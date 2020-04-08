package com.chargebee.models.enums 
sealed trait OnEvent 
case object SUBSCRIPTION_CREATION extends OnEvent 
case object SUBSCRIPTION_TRIAL_START extends OnEvent 
case object PLAN_ACTIVATION extends OnEvent 
case object SUBSCRIPTION_ACTIVATION extends OnEvent 
case object _UNKNOWN extends OnEvent 
