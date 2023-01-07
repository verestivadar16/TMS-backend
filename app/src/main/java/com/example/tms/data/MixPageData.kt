package com.example.tms.data

import android.graphics.Bitmap

class MixPageData{
    var personName: String? = null
    var postDescription: String? = null
    var imageId: Bitmap? = null
    var profImageId: Bitmap? = null

    var productimage: Bitmap?  = null
    var sellerimage: Bitmap?  = null
    var sellername: String? = null
    var productname: String?  = null
    var productdescription: String? = null
    var price: String?  = null

    var organiserPic: Int? = null
    var organiserName: String? = null
    var eventDescription: String? = null
    var locationImage: Bitmap? = null

    var warningIcon: Int? = null
    var warningName: String? = null
    var warningImage: Bitmap? = null

    var postTypeID: String? = null
    var response: String? = null

    constructor() {}

    constructor(name: String?, postDescription: String?, imageId: Bitmap?,profImageId: Bitmap?, postTypeID: String?,response: String?) {
        this.personName = name
        this.postDescription = postDescription
        this.imageId = imageId
        this.profImageId = profImageId
        this.postTypeID = postTypeID
        this.response = response

    }
    constructor(productimage: Bitmap?, sellerimage: Bitmap?, sellername: String? , productname: String?  ,productdescription: String?, price: String?, postTypeID: String?,response: String?) {
        this.productimage = productimage
        this.sellerimage = sellerimage
        this.sellername = sellername
        this.productname = productname
        this.productdescription = productdescription
        this.price = price
        this.postTypeID = postTypeID
        this.response = response
    }

    constructor(organiserPic: Int?, organiserName: String?, eventDescription: String?, locationImage: Bitmap?, postTypeID: String?,response: String?) {
        this.organiserPic = organiserPic
        this.organiserName = organiserName
        this.eventDescription = eventDescription
        this.locationImage = locationImage
        this.postTypeID = postTypeID
        this.response = response
    }

    constructor(warningIcon: Int?, warningName: String?, warningImage: Bitmap?, postTypeID: String?,response: String?) {
        this.warningIcon = warningIcon
        this.warningName = warningName
        this.warningImage = warningImage
        this.postTypeID = postTypeID
        this.response = response
    }
}
