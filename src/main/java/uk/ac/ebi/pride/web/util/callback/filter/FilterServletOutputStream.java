package uk.ac.ebi.pride.web.util.callback.filter;

/*
 * Copyright 2008-2017 by Emeric Vernat
 *
 *     This file is part of Java Melody.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

/**
 * Output stream pour un filtre implémentant {@link ServletOutputStream} à partir d'un autre
 * ServletOutputStream ou d'un OutputStream.
 * https://raw.githubusercontent.com/javamelody/javamelody/master/javamelody-core/src/main/java/net/bull/javamelody/internal/web/FilterServletOutputStream.java
 *
 * @author Emeric Vernat
 */
public class FilterServletOutputStream extends ServletOutputStream {
  private final OutputStream stream;
  private final ServletOutputStream servletOutputStream;
  private boolean closed;

  /**
   * Constructor.
   *
   * @param output ServletOutputStream
   */
  FilterServletOutputStream(ServletOutputStream output) {
    super();
    assert output != null;
    stream = output;
    servletOutputStream = output;
  }

  /**
   * Constructor.
   *
   * @param output OutputStream
   */
  public FilterServletOutputStream(OutputStream output) {
    super();
    assert output != null;
    stream = output;
    servletOutputStream = null;
  }

  /** {@inheritDoc} */
  @Override
  public void close() throws IOException {
    stream.close();
    closed = true;
  }

  /** {@inheritDoc} */
  @Override
  public void flush() throws IOException {
    if (!closed) {
      stream.flush();
    }
  }

  /** {@inheritDoc} */
  @Override
  public void write(int b) throws IOException {
    stream.write(b);
  }

  /** {@inheritDoc} */
  @Override
  public void write(byte[] bytes) throws IOException {
    stream.write(bytes);
  }

  /** {@inheritDoc} */
  @Override
  public void write(byte[] bytes, int off, int len) throws IOException {
    stream.write(bytes, off, len);
  }

  @Override
  public boolean isReady() {
    if (servletOutputStream != null) {
      return servletOutputStream.isReady();
    }
    return true;
  }

  @Override
  public void setWriteListener(WriteListener writeListener) {
    if (servletOutputStream != null) {
      servletOutputStream.setWriteListener(writeListener);
    }
  }
}
