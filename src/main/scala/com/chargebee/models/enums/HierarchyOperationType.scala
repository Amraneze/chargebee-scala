package com.chargebee.models.enums 
sealed trait HierarchyOperationType 
case object COMPLETE_HIERARCHY extends HierarchyOperationType 
case object SUBORDINATES extends HierarchyOperationType 
case object PATH_TO_ROOT extends HierarchyOperationType 
case object _UNKNOWN extends HierarchyOperationType 
