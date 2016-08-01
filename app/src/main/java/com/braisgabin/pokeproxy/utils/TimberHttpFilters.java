package com.braisgabin.pokeproxy.utils;

import org.littleshoot.proxy.HttpFilters;

import java.net.InetSocketAddress;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpResponse;
import timber.log.Timber;

public class TimberHttpFilters implements HttpFilters {
  private final String uri;

  public TimberHttpFilters(String uri) {
    this.uri = uri;
  }

  @Override
  public HttpResponse clientToProxyRequest(HttpObject httpObject) {
    Timber.v("clientToProxyRequest: %s", uri);
    return null;
  }

  @Override
  public void proxyToServerConnectionQueued() {
    Timber.v("proxyToServerConnectionQueued: %s", uri);
  }

  @Override
  public InetSocketAddress proxyToServerResolutionStarted(String resolvingServerHostAndPort) {
    Timber.v("proxyToServerResolutionStarted: %s", uri);
    return null;
  }

  @Override
  public void proxyToServerResolutionSucceeded(String serverHostAndPort, InetSocketAddress resolvedRemoteAddress) {
    Timber.v("proxyToServerResolutionSucceeded: %s", uri);
  }

  @Override
  public void proxyToServerResolutionFailed(String hostAndPort) {
    Timber.v("proxyToServerResolutionFailed: %s", uri);
  }

  @Override
  public HttpResponse proxyToServerRequest(HttpObject httpObject) {
    Timber.v("proxyToServerRequest: %s", uri);
    return null;
  }

  @Override
  public void proxyToServerConnectionStarted() {
    Timber.v("proxyToServerConnectionStarted: %s", uri);
  }

  @Override
  public void proxyToServerConnectionSSLHandshakeStarted() {
    Timber.v("proxyToServerConnectionSSLHandshakeStarted: %s", uri);
  }

  @Override
  public void proxyToServerConnectionSucceeded(ChannelHandlerContext serverCtx) {
    Timber.v("proxyToServerConnectionSucceeded: %s", uri);
  }

  @Override
  public void proxyToServerConnectionFailed() {
    Timber.v("proxyToServerConnectionFailed: %s", uri);
  }

  @Override
  public void proxyToServerRequestSending() {
    Timber.v("proxyToServerRequestSending: %s", uri);
  }

  @Override
  public void proxyToServerRequestSent() {
    Timber.v("proxyToServerRequestSent: %s", uri);
  }

  @Override
  public void serverToProxyResponseReceiving() {
    Timber.v("serverToProxyResponseReceiving: %s", uri);
  }

  @Override
  public HttpObject serverToProxyResponse(HttpObject httpObject) {
    Timber.v("serverToProxyResponse: %s", uri);
    return httpObject;
  }

  @Override
  public void serverToProxyResponseReceived() {
    Timber.v("serverToProxyResponseReceived: %s", uri);
  }

  @Override
  public void serverToProxyResponseTimedOut() {
    Timber.v("serverToProxyResponseTimedOut: %s", uri);
  }

  @Override
  public HttpObject proxyToClientResponse(HttpObject httpObject) {
    Timber.v("proxyToClientResponse: %s", uri);
    return httpObject;
  }
}
