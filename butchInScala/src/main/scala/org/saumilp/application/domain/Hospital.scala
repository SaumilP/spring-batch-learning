package org.saumilp.application.domain

import javax.xml.bind.annotation.{XmlAttribute, XmlType, XmlRootElement}

@XmlType(name = "HospitalType")
abstract class Hospital {
  @XmlAttribute var hospitalType: String = _
  @XmlAttribute var msgKey: String = _
  @XmlAttribute var tipKey: String = _

  override def toString = {
    "Hospital[hospitalType=%s, msgKey=%s, tipKey=%s]".format(hospitalType, msgKey, tipKey)
  }
}

@XmlType(name = "InHospital")
case class InHospital(hType:String, mKey:String, tKey:String) extends Hospital {
  hospitalType = hType
  msgKey = mKey
  tipKey = tKey
}

@XmlType(name = "OutHospital")
case class OutHospital(hType:String, mKey:String, tKey:String) extends Hospital {
  hospitalType = hType
  msgKey = mKey
  tipKey = tKey
}
