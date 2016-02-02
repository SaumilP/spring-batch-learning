package org.saumilp.application.service.util

import io.Source
import java.io.{File, Reader}
import scala.Left
import scala.Predef._
import scala.Right
import util.parsing.combinator.RegexParsers

class CSVParser(source:Reader) extends RegexParsers {
  lazy val result:Either[String, CSVArray] = parse(root, source) match {
    case Success(result, _) => Right(result)
    case NoSuccess(msg, in) => Left(s"$msg at line: ${in.pos.line}, column: ${in.pos.column}")
  }

  protected val DELIMITER = ','
  protected val QUOTE = '"'

  protected lazy val root =  line ~ rep(EOL ~> line) <~ (EOL ~ EOF | EOL ~ line) ^^ {
    case headers ~ rest => new CSVArray(headers :: rest)
  }
  protected lazy val line =
    field ~ rep(DELIMITER ~> field) ^? (
      {
        case f ~ xs if numFields.map(_ == xs.size + 1).getOrElse {
          numFields = Some(xs.size + 1)
          true
        } => f :: xs
      }, {
      case f ~ xs => s"${(f :: xs).size} fields found, but ${numFields.get} expected"
    }
      )
  protected lazy val field = quotedField | unQuotedField
  protected lazy val unQuotedField = guard(EOL) ^^^ "" | guard(readable) ~> rep(readableAndNot(DELIMITER)) ^^ {
    case x :: xs => x + xs.mkString
    case Nil => ""
  }
  protected lazy val quotedField = QUOTE ~> quotedText <~ QUOTE
  protected lazy val quotedText = rep(QUOTE ~ QUOTE ^^^ QUOTE | readableAndNot(QUOTE)) ^^ {
    case head :: tail => head + tail.mkString
    case _ => ""
  }
  protected lazy val EOF = Parser { in =>
    if (in.atEnd) Success((), in) else Failure(s"EOF expected, but 0x${Integer.toHexString(in.first.toInt)} found", in)
  }
  protected lazy val EOL = '\r'.? ~ '\n'
  protected lazy val readable = readableAndNot()

  override protected val whiteSpace = "".r

  protected var numFields = None: Option[Int]

  protected def readableAndNot(excluded:Char*) = Parser { in =>
    val excludedSet = excluded.toSet
    if (excludedSet(in.first) || EOL(in).successful || EOF(in).successful)
      Failure("expected any char except EOL (end of line)" +
        (if (excludedSet.nonEmpty) " and " + excludedSet.mkString(",") else ""), in)
    else
      Success(in.first, in.rest)
  }

  def this(f:File) = this(Source.fromFile(f).reader)
  def this(fileName:String) = this(new File(fileName))
}

class CSVArray(rowsList:List[List[String]]) {
  class Row(row:List[String]) {
    def get(fieldName:String) =
      headerIndex.get(fieldName.trim).flatMap { idx =>
        row.drop(idx).headOption
      }
    def asMap = headers zip row
  }
  lazy val headers = rowsList.head.map(_.trim)
  lazy val headerIndex = headers.zipWithIndex.toMap
  lazy val rows = rowsList.tail.map(new Row(_))
}