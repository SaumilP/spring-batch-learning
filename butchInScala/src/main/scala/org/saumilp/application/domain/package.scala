package org.saumilp.application

import javax.xml.bind.annotation.{XmlElements, XmlElement}
import javax.xml.bind.annotation.adapters.{XmlAdapter, XmlJavaTypeAdapter}

import scala.annotation.meta.field
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter

package object jaxb {
  type xmlElement = XmlElement @field
  type xmlTypeAdapter = XmlJavaTypeAdapter @field
  type xmlElements = XmlElements @field

  class CustomOptionAdapter[S,A](customAdapter:XmlAdapter[String,A], nones: String*)
    extends XmlAdapter[String, Option[A]] {

    def marshal(v: Option[A]): String = {
      v.map(customAdapter.marshal).getOrElse(nones(0))
    }

    def unmarshal(v: String):Option[A] = {
      if (nones contains v) None else Some(customAdapter.unmarshal(v))
    }
  }

  class DateTimeAdapter(dtf: DateTimeFormatter) extends XmlAdapter[String, DateTime] {
    def unmarshal(v: String): DateTime = dtf.parseDateTime(v)
    def marshal(v: DateTime): String = dtf.print(v)
  }

  class DateTimeOptionAdapter(dtf: DateTimeFormatter)
    extends CustomOptionAdapter[String, DateTime](new DateTimeAdapter(dtf), null, "")
}
