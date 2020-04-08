package com.chargebee.models.enums 
sealed trait FriendOfferType 
case object NONE extends FriendOfferType 
case object COUPON extends FriendOfferType 
case object COUPON_CODE extends FriendOfferType 
case object _UNKNOWN extends FriendOfferType 
