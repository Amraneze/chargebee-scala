package com.chargebee.models.enums 
sealed trait Taxability 
case object TAXABLE extends Taxability 
case object EXEMPT extends Taxability 
case object _UNKNOWN extends Taxability 
