package com.sloy.sevibus.ui.mvp.presenter;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.sloy.sevibus.resources.AnalyticsTracker;
import com.sloy.sevibus.resources.CrashReportingTool;
import com.sloy.sevibus.resources.actions.user.LogOutAction;
import com.sloy.sevibus.resources.actions.user.ObtainUserAction;
import com.sloy.sevibus.ui.SevibusUser;
import com.sloydev.gallego.Optional;

public class UserInfoHeaderPresenter implements Presenter<UserInfoHeaderPresenter.View> {

    private final ObtainUserAction obtainUserAction;
    private final LogOutAction logOutAction;
    private final AnalyticsTracker analyticsTracker;
    private final CrashReportingTool crashReportingTool;
    private final GoogleApiClient googleApiClient;
    private View view;

    public UserInfoHeaderPresenter(ObtainUserAction obtainUserAction, LogOutAction logOutAction, AnalyticsTracker analyticsTracker, CrashReportingTool crashReportingTool, GoogleApiClient googleApiClient) {
        this.obtainUserAction = obtainUserAction;
        this.logOutAction = logOutAction;
        this.analyticsTracker = analyticsTracker;
        this.crashReportingTool = crashReportingTool;
        this.googleApiClient = googleApiClient;
    }

    @Override
    public void initialize(View view) {
        this.view = view;
        retrieveUserInfo();
    }

    private void retrieveUserInfo() {
        obtainUserAction.obtainUser()
          .subscribe(optionalUser -> {
              if (optionalUser.isPresent()) {
                  view.showUserInfo(optionalUser.get());
              } else {
                  view.showSigninInstructions();
              }
          });
    }


    public void onProfileClick() {
        obtainUserAction.obtainUser()
          .map(Optional::isPresent)
          .subscribe(isLoggedIn -> {
              if (isLoggedIn) {
                  view.showLogoutOption();
              } else {
                  view.navigateToLogin();
              }
          });
    }

    public void onSignOutClick() {
        analyticsTracker.signInLogout();
        logOutAction.logOut().subscribe();
        view.showSigninInstructions();
        // Sorry, clean code
        Auth.GoogleSignInApi.signOut(googleApiClient);
    }

    @Override
    public void update() {
        retrieveUserInfo();
    }

    @Override
    public void pause() {
        //NA
    }

    public interface View {

        void showUserInfo(SevibusUser user);

        void showSigninInstructions();

        void navigateToLogin();

        void showLogoutOption();
    }

}
