package org.saumilp.application.domain

import javax.xml.bind.annotation._
import org.saumilp.application.jaxb.{xmlElement, xmlElements}

import scala.beans.BeanProperty

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "Network")
case class Network(@XmlAttribute(name = "id") @BeanProperty var networkId:String,
                   @XmlAttribute @BeanProperty var coverId:String)

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "Cover")
case class Cover(@XmlAttribute var id:String,
                 @BeanProperty var coverType:String,
                 @BeanProperty var benefit:String,
                 @BeanProperty var msg:String,
                 @BeanProperty var tip:String,
                 @xmlElements(
                  value = Array(new xmlElement(name="IH", `type` = classOf[InHospital]),
                                new xmlElement(name="OH", `type` = classOf[OutHospital]))
                 ) var hospitalType:Hospital )

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "OrganisationType", propOrder = Array("networks"))
case class OrganisationType(@XmlElement(name = "Network") @BeanProperty var networks:List[Network],
                            @XmlAttribute @BeanProperty var value:String,
                            @XmlAttribute @BeanProperty var defaultCoverId: String)

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "Plan", propOrder = Array("organisationTypes"))
case class Plan(@XmlElement(name = "OrganisationType") @BeanProperty var organisationTypes:List[OrganisationType],
                @XmlAttribute(name = "code") @BeanProperty var planCode:String,
                @XmlAttribute @BeanProperty var effectiveFrom:String,
                @XmlAttribute @BeanProperty var effectiveTo: String)


object PlanBenefitCoverageByOrganisationType {
  final val XML_ROOT = "PlanBenefitCoverageByOrganisationType"
}

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = PlanBenefitCoverageByOrganisationType.XML_ROOT)
@XmlType(propOrder = Array("plans", "covers", "messages", "tipMessages"))
case class PlanBenefitCoverageByOrganisationType(@XmlElement(name = "Plan") @BeanProperty var plans:List[Plan],
                                                  @XmlElement(name = "Msg") @BeanProperty var messages:Array[Message],
                                                  @XmlElement(name = "Tip") @BeanProperty var tipMessages:Array[Tip],
                                                  @XmlElement(name = "Cover") @BeanProperty var covers:Array[Cover]){
}

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "Message", propOrder = Array("message"))
case class Message(@XmlAttribute(name = "key") @BeanProperty var key:String,
                   @XmlValue @BeanProperty var message:String)

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "Tip", propOrder = Array("tipMessage"))
case class Tip(@XmlAttribute(name="key") @BeanProperty var key:String,
               @XmlValue @BeanProperty var tipMessage:String)