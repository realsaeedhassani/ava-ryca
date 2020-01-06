//package com.lauzy.freedom.lyricview.retro;
//
//public class ExampleActivity {
//    private ExampleNetwork mExampleNetwork;
//
//    private void getDetails(String id) {
//        mExampleNetwork.getCachedDetails(id) // From Cache
//                .doFinally(() -> {
//                    mExampleNetwork
//                            .getDetails(id) // From Network
//                            .subscribe(details -> onDetailsReceived(details), this::onError);
//                })
//                .subscribe(details -> onDetailsReceived(details), this::onError);
//    }
//
//    private void onDetailsReceived(Details details) {
//        if (mDetails == null || !mDetails.equals(details)) { // Implement equals with your criteria
//            mDetails = details;
//            updateUI();
//        }
//    }
//}
