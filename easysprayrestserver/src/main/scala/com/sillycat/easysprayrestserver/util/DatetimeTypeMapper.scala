package com.sillycat.easysprayrestserver.util

import java.sql.Date
import java.sql.Timestamp
import scala.slick.lifted.BaseTypeMapper
import scala.slick.lifted.MappedTypeMapper
import org.joda.time.DateTime
import com.sillycat.easysprayrestserver.model.UserType

object JodaTimestampMapper extends MappedTypeMapper[DateTime, Timestamp] with BaseTypeMapper[DateTime] {
  def map(j: DateTime) = new Timestamp(j.getMillis)
  def comap(s: Timestamp) = new DateTime(s.getTime)
  override def sqlType = Some(java.sql.Types.TIMESTAMP)
  override def sqlTypeName = Some("timestamp")
}

object JodaDateTimeMapper extends MappedTypeMapper[DateTime, Date] with BaseTypeMapper[DateTime] {
  def map(j: DateTime) = new Date(j.getMillis)
  def comap(s: Date) = new DateTime(s.getTime)
  override def sqlType = Some(java.sql.Types.DATE)
  override def sqlTypeName = Some("date")
}

object UserTypeMapper extends MappedTypeMapper[UserType.Value, String] with BaseTypeMapper[UserType.Value] {
  def map(j: UserType.Value) = j.toString()
  def comap(s: String) = UserType.withName(s)
  override def sqlType = Some(java.sql.Types.NCHAR)
  override def sqlTypeName = Some("userType")
}