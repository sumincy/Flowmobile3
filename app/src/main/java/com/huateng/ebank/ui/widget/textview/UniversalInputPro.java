package com.huateng.ebank.ui.widget.textview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.huateng.ebank.R;
import com.huateng.ebank.app.Constants;
import com.huateng.ebank.ui.widget.wheel.OnRangeTimeSelectListener;
import com.huateng.ebank.ui.widget.wheel.RangeTimePickerBuilder;
import com.huateng.ebank.ui.widget.wheel.RangeTimePickerView;
import com.huateng.ebank.ui.widget.wheel.SelectedDate;
import com.tools.utils.StringUtils;
import com.tools.view.RxToast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author shanyong
 * @description 通用输入框
 */
public class UniversalInputPro extends AppCompatEditText {

    private final String TAG = getClass().getSimpleName();
    private Context context;
    private String mLabelText;
    float baseLine = 0;
    private int mLabelPadding;
    private boolean mRequired;
    private int mRequireRightPadding, mOptionIconPadding;
    private int mInitLeftPadding, mInitRightPadding, mInitTopPadding, mInitBottomPadding;
    private Drawable mOptionIconDrawable;
    private int mOptionIconSize = 0;
    private float mOptionIconLeft, mOptionIconTop;
    private OnHtOptionTouchListener onHtOptionTouchListener;
    private int mInputType;
    private int imageSrcId;
    //    public static final int TEXT = 0, EDIT_TEXT = 1, SPINNER = 2, DATE = 3, TIME = 4;
    private final int WRAP_CONTENT = -1;
    private int mLabelTextSize;

    private OptionsPickerView pvSpinnerOptions;
    private boolean isChild = false;

    private TimePickerView pvTime;
    private RangeTimePickerView pvRangeTime;
    private List<String> mItemList;

    private TextWatcher tw;
    private int dimen2dp;

    private List<String> mEmptyList = new ArrayList<>();

    private OnOptionIconClickListener onOptionIconClickListener;
    private int mLabelColor, mLabelWidth;

    /**
     * Keep track of which icon we used last
     */
    private Drawable lastErrorIcon = null;

    public UniversalInputPro(Context context) {
        super(context);
        this.context = context;
        init(null);
    }

    public UniversalInputPro(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    public UniversalInputPro(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init(attrs);
    }

    public void setChild(boolean child) {
        isChild = child;
    }


    private void init(AttributeSet attrs) {
        dimen2dp = (int) getResources().getDimension(R.dimen.d_2dp);
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.UniversalInput);
            mLabelText = a.getString(R.styleable.UniversalInput_labelText);
            //正文文字距标签距离
            mLabelPadding = (int) a.getDimension(R.styleable.UniversalInput_labelPadding, dimen2dp);
            mRequired = a.getBoolean(R.styleable.UniversalInput_required, false);
            mOptionIconDrawable = a.getDrawable(R.styleable.UniversalInput_optionIcon);
            mLabelColor = a.getColor(R.styleable.UniversalInput_labelColor, getResources().getColor(R.color.gary_textcolor));
            mInputType = a.getInt(R.styleable.UniversalInput_inputType, UniversalInputType.TEXT);
            mLabelWidth = (int) a.getDimension(R.styleable.UniversalInput_labelWidth, WRAP_CONTENT);
            imageSrcId = a.getResourceId(R.styleable.UniversalInput_imageSrc, R.mipmap.ic_launcher);
            mLabelTextSize = (int) a.getDimension(R.styleable.UniversalInput_labelTextSize, getTextSize());
            a.recycle();
        }
        initInputType();
        setSingleLine(false);
        //若是WRAP_CONTENT 则需测量标签宽度
        if (mLabelWidth == WRAP_CONTENT) {
            measuredLabelWidth();
        }
        if (mRequired) {
            mRequireRightPadding = (int) getResources().getDimension(R.dimen.d_15dp);
        }
        if (mOptionIconDrawable != null) {
            mOptionIconPadding = (int) getResources().getDimension(R.dimen.d_15dp);
        }
        mInitTopPadding = getPaddingTop();
        mInitRightPadding = getPaddingRight();
        mInitLeftPadding = getPaddingLeft();
        mInitBottomPadding = getPaddingBottom();
        setPadding(mLabelWidth + mLabelPadding + mInitLeftPadding + 2 * dimen2dp, mInitTopPadding, mInitRightPadding + mRequireRightPadding + mOptionIconPadding + mOptionIconSize,
                mInitBottomPadding);

    }

    public void setUniversalInputType(int inputType) {
        mInputType = inputType;
        initInputType();
        invalidate();
        requestLayout();
    }

    private void initInputType() {
        switch (mInputType) {
            case UniversalInputType.TEXT:
                setEnabled(false);
                break;
            case UniversalInputType.SPINNER:
                setEnabled(false);
                mOptionIconSize = (int) getResources().getDimension(R.dimen.universal_input_icon_size_larger);
                mOptionIconDrawable = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.mipmap.spinner_arrow));
                initSpinner();
                break;
            case UniversalInputType.DATE.DATE_SINGEL:
                setEnabled(false);
                mOptionIconSize = (int) getResources().getDimension(R.dimen.universal_input_icon_size_larger);
                if (mOptionIconDrawable == null) {
                    mOptionIconDrawable = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.mipmap.picker_calendar));
                }
                initDateTimePicker();
                break;
            case UniversalInputType.DATE.DATE_RANGE:
                setEnabled(false);
                mOptionIconSize = (int) getResources().getDimension(R.dimen.universal_input_icon_size_larger);
                if (mOptionIconDrawable == null) {
                    mOptionIconDrawable = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.mipmap.picker_calendar));
                }
                initRangeDateTimePicker();
                break;
            case UniversalInputType.DATE.TIME:
                mOptionIconSize = (int) getResources().getDimension(R.dimen.universal_input_icon_size_larger);
                mOptionIconDrawable = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.mipmap.time));
                setEnabled(false);
                initDateTimePicker();
                break;
            case UniversalInputType.IMAGE:
                mOptionIconSize = (int) getResources().getDimension(R.dimen.universal_input_icon_size_larger);
                mOptionIconDrawable = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), imageSrcId));
                setEnabled(true);
                break;
            case UniversalInputType.EDIT.TEXT:
                setInputType(EditorInfo.TYPE_CLASS_TEXT);
                initEditInputType();
                break;
            case UniversalInputType.EDIT.DECIMAL:
                setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
                initEditInputType();
                break;
            case UniversalInputType.EDIT.TELEPHONE:
                setInputType(EditorInfo.TYPE_CLASS_NUMBER);
                initEditInputType();
                break;
            case UniversalInputType.EDIT.NUMBER:
                setInputType(EditorInfo.TYPE_CLASS_NUMBER);
                initEditInputType();
                break;
            case UniversalInputType.EDIT.PASSWARD:
                setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                initEditInputType();
                break;
        }
    }

    //初始化spinner
    private void initSpinner() {
        pvSpinnerOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                String item = mItemList.get(options1);
//                setText(item);
                if (!TextUtils.isEmpty(item)) {
                    if (Constants.DEFAULT_DIC.equals(item)) {
                        setText(null);
                    } else {
                        setText(item);
                    }
                }

                //错误重置
                if (mRequired && !TextUtils.isEmpty(item) && isErrorShown()) {
                    setError(null);
                }

                if (onValueChangeListener != null && !Constants.DEFAULT_DIC.equals(item)) {
                    onValueChangeListener.onValueChanged(item);
                }
                if (onItemSelectedListener != null) {
                    onItemSelectedListener.onItemSelected(options1);
                }
            }
        }).setLayoutRes(R.layout.pickerview_options, new CustomListener() {
            @Override
            public void customLayout(View v) {
                final TextView tvSubmit = v.findViewById(R.id.tv_finish);
                ImageView ivCancel = v.findViewById(R.id.iv_cancel);
                tvSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvSpinnerOptions.returnData();
                        pvSpinnerOptions.dismiss();
                    }
                });

                ivCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvSpinnerOptions.dismiss();
                    }
                });

            }
        }).build();
    }

    public UniversalInputPro setDataSource(List<String> itemList) {
        this.mItemList = itemList;
        pvSpinnerOptions.setPicker(mItemList);//添加数据
        return this;
    }

    public void setSelectOptions(int position) {
        pvSpinnerOptions.setSelectOptions(position);
    }

    public void clearDataSource() {
        setText(null);
        this.mItemList = mEmptyList;
        pvSpinnerOptions.setPicker(mEmptyList);
    }

    public void initEditInputType() {
        setEnabled(true);
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (mRequired && !TextUtils.isEmpty(s) && isErrorShown()) {
                    setError(null);
                }

                if (onValueChangeListener != null) {
                    onValueChangeListener.onValueChanged(s.toString());
                }
            }
        });
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 获取标签宽度  默认WRAP_CONTENT时调用
     */
    private int measuredLabelWidth() {
        if (mLabelText != null && !TextUtils.isEmpty(mLabelText)) {
            Paint textPaint = new Paint();
            textPaint.setTextSize(mLabelTextSize);
            mLabelWidth = (int) (textPaint.measureText(mLabelText));
            return mLabelWidth;
        }
        return 0;
    }

    public String getNoneNullText() {
        if (TextUtils.isEmpty(getText())) {
            if (mRequired) {
                setError("输入不能为空");
            }
            return "";
        } else {
            return getText().toString();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mLabelText != null) {
            drawLabel(canvas);
        }
        if (mOptionIconDrawable != null) {
            drawOptionIcon(canvas);
        }
        super.onDraw(canvas);

    }

    //label
    private void drawLabel(Canvas canvas) {
        Paint textPaint = new Paint();
        textPaint.setColor(mLabelColor);
        textPaint.setTextSize(mLabelTextSize);
        if (baseLine == 0) {
            Paint.FontMetrics metrics = textPaint.getFontMetrics();
            baseLine = getMeasuredHeight() / 2 + (metrics.bottom - metrics.top)
                    / 2 - metrics.bottom;
        }
        int measuredWith = (int) (textPaint.measureText(mLabelText));
//        canvas.drawText(mLabelText,getScrollX(), baseLine, textPaint);
        if (mLabelWidth == WRAP_CONTENT) {
            canvas.drawText(mLabelText, getScrollX(), baseLine, textPaint);
        } else {
            canvas.drawText(mLabelText, mLabelWidth - measuredWith, baseLine, textPaint);
        }
    }

    //icon
    private void drawOptionIcon(Canvas canvas) {
        Paint textPaint1 = new Paint();
        textPaint1.setTextSize(30);
        BitmapDrawable bd = (BitmapDrawable) mOptionIconDrawable;
        Bitmap bitmap = bd.getBitmap();
        Bitmap newBitmap = getResizedBitmap(bitmap, mOptionIconSize, mOptionIconSize);
        if (mRequired) {
            mOptionIconLeft = (getMeasuredWidth() - textPaint1.measureText("*") - 40)
                    + getScrollX() - mOptionIconSize - 40;
        } else {
            mOptionIconLeft = getMeasuredWidth()
                    + getScrollX() - mOptionIconSize - 40;
        }

        mOptionIconTop = (getMeasuredHeight() - mOptionIconSize) / 2;
        canvas.drawBitmap(newBitmap, mOptionIconLeft, mOptionIconTop, null);
    }

    public interface OnHtOptionTouchListener {
        void onHtOptionTouched();
    }

    public void setOnHtOptionTouchListener(OnHtOptionTouchListener onHtOptionTouchListener) {
        this.onHtOptionTouchListener = onHtOptionTouchListener;
    }

    private float mTouchHelpOffset = 20;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                float y = event.getY();
                if (mOptionIconDrawable != null && x > mOptionIconLeft - mTouchHelpOffset && x < mOptionIconLeft + mOptionIconSize + mTouchHelpOffset
                        && y > mOptionIconTop - mTouchHelpOffset && y < mOptionIconTop + mOptionIconSize + mTouchHelpOffset) {
                    if (onHtOptionTouchListener != null) {
                        onHtOptionTouchListener.onHtOptionTouched();
                    }
                    requestFocus();
                    requestFocusFromTouch();
                    if (mInputType == UniversalInputType.SPINNER) {
                        showSpinner();
                    } else if (mInputType == UniversalInputType.DATE.DATE_SINGEL || mInputType == UniversalInputType.DATE.TIME) {
                        showDateTimePicker();
                    } else if (mInputType == UniversalInputType.DATE.DATE_RANGE) {
                        showRangeDateTimePicker();
                    } else if (mInputType == UniversalInputType.IMAGE) {
                        //TODO
                        if (onOptionIconClickListener != null) {
                            onOptionIconClickListener.onClick();
                        }
                    }
                    //Toast.makeText(context, "点击了OptionIcon",Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    public interface OnOptionIconClickListener {
        void onClick();
    }

    public void setOnOptionIconClickListener(OnOptionIconClickListener listener) {
        this.onOptionIconClickListener = listener;
    }


    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scale;
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        if (scaleWidth >= scaleHeight) {
            scale = scaleHeight;
        } else {
            scale = scaleWidth;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    public void setLabel(String labelText) {
        this.mLabelText = labelText;
        invalidate();
        measuredLabelWidth();
        setPadding(mLabelWidth + mLabelPadding + mInitLeftPadding + 2 * dimen2dp, getPaddingTop(), getPaddingRight(),
                getPaddingBottom());
    }

    private void showSpinner() {
        if (pvSpinnerOptions != null) {
            if (null != mItemList && mItemList.size() > 0) {
                pvSpinnerOptions.show();
            } else {
                if (isChild) {
                    RxToast.showToast("请选择上一级选项");
                } else {
                    RxToast.showToast("没有数据");
                }
            }
        }
    }

    /*********************
     * Date Time related
     ********************/

    private SelectedDate mSelectedDate;


    private void initDateTimePicker() {
        mSelectedDate = new SelectedDate(Calendar.getInstance());
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11

        Calendar startDate = Calendar.getInstance();
        startDate.set(2000, 0, 23);
        Calendar selectedDate = Calendar.getInstance();

        //时间选择器
        pvTime = new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null

                Calendar sCalendar = Calendar.getInstance();
                sCalendar.setTime(date);
                mSelectedDate = new SelectedDate(sCalendar);
                String time = formatTime(date);
                setText(time);

                //错误重置
                if (mRequired && !TextUtils.isEmpty(time) && isErrorShown()) {
                    setError(null);
                }

                if (onValueChangeListener != null) {
                    onValueChangeListener.onValueChanged(time);
                }
            }
        }).setLayoutRes(R.layout.pickerview_time, new CustomListener() {

            @Override
            public void customLayout(View v) {
                final TextView tvSubmit = v.findViewById(R.id.tv_finish);
                TextView tvTitle = v.findViewById(R.id.tv_title);
                tvTitle.setText("请选择日期");
                ImageView ivCancel = v.findViewById(R.id.iv_cancel);
                tvSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvTime.returnData();
                        pvTime.dismiss();
                    }
                });
                ivCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvTime.dismiss();
                    }
                });
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("", "", "", "", "", "") //设置空字符串以隐藏单位提示   hide label
                .setContentTextSize(20)
                .setDate(selectedDate)
                .setRangDate(startDate, selectedDate)
                .build();

        pvTime.setKeyBackCancelable(false);//系统返回键监听屏蔽掉
    }


    private void initRangeDateTimePicker() {
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11

        Calendar startDate = Calendar.getInstance();
        startDate.set(2000, 0, 23);
        Calendar selectedDate = Calendar.getInstance();

        mSelectedDate = new SelectedDate(selectedDate, selectedDate);

        //时间选择器
        pvRangeTime = new RangeTimePickerBuilder(context, new OnRangeTimeSelectListener() {

            @Override
            public void onTimeSelect(SelectedDate date, View v) {
                //选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null

                mSelectedDate = date;

                String time = formatTime(null);

                setText(time);

                //
                if (mRequired && !TextUtils.isEmpty(time) && isErrorShown()) {
                    setError(null);
                }

                if (onValueChangeListener != null) {
                    onValueChangeListener.onValueChanged(time);
                }
            }

        }).setLayoutRes(R.layout.pickerview_time_range, new CustomListener() {

            @Override
            public void customLayout(View v) {
                final TextView tvSubmit = v.findViewById(R.id.tv_finish);
                TextView tvTitle = v.findViewById(R.id.tv_title);
                tvTitle.setText("请选择日期");
                ImageView ivCancel = v.findViewById(R.id.iv_cancel);
                tvSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //获取选择的时间
                        SelectedDate selectDate = pvRangeTime.wheelTime.getTime();

                        int i = SelectedDate.compareDates(selectDate.getFirstDate(), selectDate.getSecondDate());
                        if (i == -1 || i == 0) {
                            pvRangeTime.returnData();
                            pvRangeTime.dismiss();
                        } else {
                            //将时间重置
                            Toast.makeText(context, "开始时间不能大于结束时间", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                ivCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvRangeTime.dismiss();
                    }
                });
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("", "", "", "", "", "") //设置空字符串以隐藏单位提示   hide label
                .setContentTextSize(20)
                .setDate(selectedDate)
                .setRangDate(startDate, selectedDate)
                .build();

        pvRangeTime.setKeyBackCancelable(false);//系统返回键监听屏蔽掉
    }

    private void showDateTimePicker() {
        if (pvTime != null) {
            pvTime.show();
        }
    }

    private void showRangeDateTimePicker() {
        if (pvRangeTime != null) {
            pvRangeTime.show();
        }
    }

    private String formatTime(Date selectDate) {
        String text = null;
        Calendar calendar = Calendar.getInstance();

        if (selectDate != null) {
            calendar.setTime(selectDate);
            text = String.format("%s-%s-%s", calendar.get(Calendar.YEAR), StringUtils.repairDouble((calendar.get(Calendar.MONTH) + 1)),
                    StringUtils.repairDouble(calendar.get(Calendar.DAY_OF_MONTH)));
        }

        if (mInputType == UniversalInputType.DATE.DATE_SINGEL) {
            //DO

        } else if (mInputType == UniversalInputType.DATE.DATE_RANGE) {
            //TODO  日期区间选择
            Calendar startDate = mSelectedDate.getFirstDate();
            String date1 = String.format("%s/%s/%s", startDate.get(Calendar.YEAR), StringUtils.repairDouble((startDate.get(Calendar.MONTH) + 1)),
                    StringUtils.repairDouble(startDate.get(Calendar.DAY_OF_MONTH)));

            Calendar endDate = mSelectedDate.getSecondDate();
            String date2 = String.format("%s/%s/%s", endDate.get(Calendar.YEAR), StringUtils.repairDouble((endDate.get(Calendar.MONTH) + 1)),
                    StringUtils.repairDouble(endDate.get(Calendar.DAY_OF_MONTH)));

            if (startDate.getTime().getTime() < endDate.getTime().getTime()) {
                text = String.format("%s-%s", date1, date2);
            } else if (date1.equals(date2)) {
                text = date1;
            } else {
                RxToast.showToast("开始时间不能大于结束时间");
                mSelectedDate = null;
            }

        } else if (mInputType == UniversalInputType.DATE.TIME) {
            text = String.format("%s %s:%s:00", text, StringUtils.repairDouble(calendar.get(Calendar.HOUR_OF_DAY)), StringUtils.repairDouble(calendar.get(Calendar.MINUTE)));
        }

        return text;
    }


    public SelectedDate getSelectedDate() {
        return mSelectedDate;
    }

    private Pair<Boolean, SublimeOptions> getOptions() {
        SublimeOptions options = new SublimeOptions();
        int displayOptions = 0;
        if (mInputType == UniversalInputType.DATE.DATE_SINGEL || mInputType == UniversalInputType.DATE.DATE_RANGE) {
            displayOptions |= SublimeOptions.ACTIVATE_DATE_PICKER;
            options.setPickerToShow(SublimeOptions.Picker.DATE_PICKER);
            if (mInputType == UniversalInputType.DATE.DATE_RANGE) {
                options.setCanPickDateRange(true);
            }
        } else {
            displayOptions |= SublimeOptions.ACTIVATE_DATE_PICKER;
            displayOptions |= SublimeOptions.ACTIVATE_TIME_PICKER;
            options.setPickerToShow(SublimeOptions.Picker.TIME_PICKER);
        }
        options.setDisplayOptions(displayOptions);
        return new Pair<>(displayOptions != 0 ? Boolean.TRUE : Boolean.FALSE, options);
    }

    private OnValueChangeListener onValueChangeListener;
    private SpinnerItemSelectedListener onItemSelectedListener;

    public interface OnValueChangeListener {
        void onValueChanged(String value);
    }

    /**
     * item选中监听器
     */
    public interface SpinnerItemSelectedListener {
        void onItemSelected(int position);
    }

    public void setOnValueChangeListener(OnValueChangeListener onValueChangeListener) {
        this.onValueChangeListener = onValueChangeListener;
    }

    public void setOnItemSelectedListener(
            SpinnerItemSelectedListener listener) {
        this.onItemSelectedListener = listener;
    }


    /**
     * Resolve an issue where the error icon is hidden under some cases in JB
     * due to a bug http://code.google.com/p/android/issues/detail?id=40417
     */
    @Override
    public void setError(CharSequence error, Drawable icon) {
        super.setError(error, icon);
        lastErrorIcon = icon;

        // if the error is not null, and we are in JB, force
        // the error to show
        if (error != null /* !isFocused() && */) {
            showErrorIconHax(icon);
        }
    }


    /**
     * In onFocusChanged() we also have to reshow the error icon as the Editor
     * hides it. Because Editor is a hidden class we need to cache the last used
     * icon and use that
     */
    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        showErrorIconHax(lastErrorIcon);
    }


    /**
     * Use reflection to force the error icon to show. Dirty but resolves the
     * issue in 4.2
     */
    private void showErrorIconHax(Drawable icon) {
        if (icon == null)
            return;

        // only for JB 4.2 and 4.2.1
        if (android.os.Build.VERSION.SDK_INT != Build.VERSION_CODES.JELLY_BEAN
                && android.os.Build.VERSION.SDK_INT != Build.VERSION_CODES.JELLY_BEAN_MR1)
            return;

        try {
            Class<?> textview = Class.forName("android.widget.TextView");
            Field tEditor = textview.getDeclaredField("mEditor");
            tEditor.setAccessible(true);
            Class<?> editor = Class.forName("android.widget.Editor");
            Method privateShowError = editor.getDeclaredMethod("setErrorIcon",
                    Drawable.class);
            privateShowError.setAccessible(true);
            privateShowError.invoke(tEditor.get(this), icon);
        } catch (Exception e) {
            // e.printStackTrace(); // oh well, we tried
        }
    }

    public boolean isErrorShown() {
        return !TextUtils.isEmpty(getError());
    }

}
