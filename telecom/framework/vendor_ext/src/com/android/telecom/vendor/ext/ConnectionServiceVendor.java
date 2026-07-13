/*
 * Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
 * SPDX-License-Identifier: BSD-3-Clause-Clear
 */

package com.android.telecom.vendor.ext;

import android.telecom.ConnectionService;
import android.telecom.Conference;
import android.telecom.Connection.RttTextStream;
import android.util.Log;

/**
 * Provides bridging interfaces used to bypass Telecom Mainline APex visibility restrictions.
 * This class provides access to hidden APIs within the restricted Telecom module
 * and can be used to override hidden API behavior outside of the Telecom module
 */
public class ConnectionServiceVendor {

    private static final String TAG = "ConnSrvVendor";
    private Delegate mDelegate;
    private ConnectionService mService;
    private VendorExtImpl mExt;

    public ConnectionServiceVendor(Delegate delegate, ConnectionService service) {
        mExt = new VendorExtImpl();
        mDelegate = delegate;
        mService = service;
        mService.setVendorExt(mExt);
    }

    public void answerVideo(String callId, int videoState) {
        if (mService != null) {
            mService.answerVideoBase(callId, videoState);
        }
    }

    public void answer(String callId) {
        if (mService != null) {
            mService.answerBase(callId);
        }
    }

    public interface Delegate {
        void answer(String callId);
        void answerVideo(String callId, int videoState);
        void onStartRtt(Conference conference, RttTextStream rttTextStream);
        void onStopRtt(Conference conference);
    }

    public class VendorExtImpl implements ConnectionService.VendorExt {
        @Override
        public void answer(String callId) {
            if (mDelegate != null) {
                Log.i(TAG, "answer() callId: " + callId);
                mDelegate.answer(callId);
            }
        }

        @Override
        public void answerVideo(String callId, int videoState) {
            if (mDelegate != null) {
                Log.i(TAG, "answerVideo() callId: " + callId);
                mDelegate.answerVideo(callId, videoState);
            }
        }

        @Override
        public void onStartRtt(Conference conference, RttTextStream rttTextStream) {
            if (mDelegate != null) {
                Log.i(TAG, "onStartRtt()");
                mDelegate.onStartRtt(conference, rttTextStream);
            }
        }

        @Override
        public void onStopRtt(Conference conference) {
            if (mDelegate != null) {
                Log.i(TAG, "onStopRtt()");
                mDelegate.onStopRtt(conference);
            }
        }
    }
}
