package org.saumilp.application.service.util

import java.io.{File, FileOutputStream, PrintStream, OutputStream}

object IOUtil {
  def withAdditionalOut[T](out:OutputStream)(f: => T):T = {
    val oldOut = System.out
    def mkMultiOut(outs: OutputStream*) = new OutputStream {
      def write(p: Int) {
        outs.foreach(_.write(p))
      }
    }
    try {
      System.setOut(new PrintStream(mkMultiOut(System.out, out)))
      f
    } finally {
      out.close()
      System.setOut(oldOut)
    }
  }

  def withAdditionalOut[T](outFile:File)(f: => T):T = withAdditionalOut(new FileOutputStream(outFile))(f)
  def withAdditionalOut[T](outFileName:String)(f: => T):T = withAdditionalOut(new File(outFileName))(f)
}
