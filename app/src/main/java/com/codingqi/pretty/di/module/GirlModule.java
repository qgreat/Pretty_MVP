package com.codingqi.pretty.di.module;

import com.codingqi.pretty.mvp.contract.UserContract;
import com.codingqi.pretty.mvp.model.GirlsModel;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jess on 9/4/16 11:10
 * Contact with jess.yan.effort@gmail.com
 */
@Module
public class GirlModule {
    private UserContract.View view;

    /**
     * 构建UserModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     * @param view
     */
    public GirlModule(UserContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    UserContract.View provideUserView(){
        return this.view;
    }

    @ActivityScope
    @Provides
    UserContract.Model provideUserModel(GirlsModel model){
        return model;
    }
}
