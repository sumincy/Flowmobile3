package com.huateng.ebank.ui.main.vm;

import android.app.Application;
import android.support.annotation.NonNull;

import com.huateng.ebank.ui.base.BaseNetworkViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shanyong on 2019/11/15.
 */
public class LoanViewModel extends BaseNetworkViewModel {

    public List recommandProducts = new ArrayList();

    public LoanViewModel(@NonNull Application application) {
        super(application);

        //贷款
        recommandProducts.add(new Object());
        recommandProducts.add(new Object());
        recommandProducts.add(new Object());
    }


}
