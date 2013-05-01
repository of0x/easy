package com.sillycat.easysprayrestserver.service.auth

import scala.concurrent.Future

import com.sillycat.easysprayrestserver.dao.BaseDAO
import com.sillycat.easysprayrestserver.dao.BaseDAO.threadLocalSession
import com.sillycat.easysprayrestserver.model.User

import spray.http.BasicHttpCredentials
import spray.http.HttpHeaders.Authorization
import spray.routing.AuthenticationFailedRejection
import spray.routing.AuthenticationRequiredRejection
import spray.routing.HttpService
import spray.routing.RequestContext
import spray.routing.authentication.Authentication
import spray.routing.authentication.UserPass
import spray.util.executionContextFromActorRefFactory
import spray.util.pimpSeq

trait AuthenticationDirectives {
  this: HttpService =>

  def doAuthenticate(userName: String, password: String): Future[Option[User]]

  def adminOnly: RequestContext => Future[Authentication[User]] = {
    ctx: RequestContext =>
      val userPass = getToken(ctx)
      if (userPass.isEmpty)
        Future(Left(AuthenticationRequiredRejection("https", "sillycat")))
      else doAuthenticate(userPass.get.user, userPass.get.pass).map {
        user =>
          if (user.isDefined)
            Right(user.get)
          else
            Left(AuthenticationFailedRejection("sillycat"))
      }
  }

  def customerOnly: RequestContext => Future[Authentication[User]] = {
    ctx: RequestContext =>
      val userPass = getToken(ctx)
      if (userPass.isEmpty)
        Future(Left(AuthenticationRequiredRejection("https", "sillycat")))
      else doAuthenticate(userPass.get.user, userPass.get.pass).map {
        user =>
          if (user.isDefined)
            Right(user.get)
          else
            Left(AuthenticationFailedRejection("sillycat"))
      }
  }

  def withRole(roleCode: String): RequestContext => Future[Authentication[User]] = {
    ctx: RequestContext =>
      val userPass = getToken(ctx)
      if (userPass.isEmpty)
        Future(Left(AuthenticationRequiredRejection("https", "sillycat")))
      else doAuthenticate(userPass.get.user, userPass.get.pass).map {
        user =>
          if (user.isDefined)
            Right(user.get)
          else
            Left(AuthenticationFailedRejection("sillycat"))
      }
  }

  def getToken(ctx: RequestContext): Option[UserPass] = {
    val authHeader = ctx.request.headers.findByType[Authorization]
    val credentials = authHeader.map { case Authorization(creds) => creds }
    credentials.flatMap {
      case BasicHttpCredentials(user, pass) => Some(UserPass(user, pass))
      case _ => None
    }
  }
}

trait UsersAuthenticationDirectives
  extends AuthenticationDirectives {
  this: HttpService =>

  import BaseDAO.threadLocalSession

  implicit val dao: BaseDAO = BaseDAO.apply

  override def doAuthenticate(userName: String, password: String) = {
    Future {
      dao.db.withSession{
    	  dao.Users.auth(userName, password)
      }
    }
  }
}