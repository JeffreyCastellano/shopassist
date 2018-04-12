package com.fa.google.shopassist.nfc;

import com.fa.google.shopassist.models.NFCTag;

/**
 * Created by stevensanborn on 12/1/14.
 */
public interface NFCListener {

    public void onNFCTag(NFCTag tag);

}
