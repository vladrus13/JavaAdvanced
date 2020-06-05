#!/bin/bash

cd ../../../../..
kill -9 $(fuser -vn tcp 1099)
kill -9 $(fuser -vn tcp 8888)
rmiregistry &
java ru.ifmo.rain.kuznetsov.bank.server.Server $@
