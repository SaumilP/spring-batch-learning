package org.saumilp.application.web

import java.util.concurrent.atomic.AtomicLong

import org.saumilp.application.domain.Greeting
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class GreetingController {
  val counter = new AtomicLong()

  @RequestMapping(value = Array("/greeting"))
  def greeting(@RequestParam(value = "name", defaultValue = "User") name:String ) = {
    val id = counter.incrementAndGet()
    Greeting(id, s"Hello, $name")
  }
}
