package org.saumilp.application

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class ButchWebApplication

object Application {
  def main (args:Array[String]):Unit =
    SpringApplication.run(classOf[ButchWebApplication])
}