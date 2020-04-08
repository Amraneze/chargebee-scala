package com.chargebee.models.enums 
sealed trait Source 
case object ADMIN_CONSOLE extends Source 
case object API extends Source 
case object SCHEDULED_JOB extends Source 
case object HOSTED_PAGE extends Source 
case object PORTAL extends Source 
case object SYSTEM extends Source 
case object NONE extends Source 
case object JS_API extends Source 
case object MIGRATION extends Source 
case object BULK_OPERATION extends Source 
case object EXTERNAL_SERVICE extends Source 
case object _UNKNOWN extends Source 
