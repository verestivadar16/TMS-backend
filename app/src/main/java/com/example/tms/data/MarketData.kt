package com.example.tms.data

import android.graphics.Bitmap

data class MarketData(val sellerimage: Bitmap,
                      val sellername: String,
                      val sellerUid: String,
                      val productimage: Bitmap,
                      val productname: String,
                      val productdescription: String,
                      val location: String,
                      val price: String)
