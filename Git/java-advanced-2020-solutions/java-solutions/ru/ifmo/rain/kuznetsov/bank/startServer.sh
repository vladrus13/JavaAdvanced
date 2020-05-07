#!/bin/bash

cd ../../../../..
rmi = $(pidof rmiregistry)
[ -z $rmi ] && rmiregistry &
java ru.ifmo.rain.kuznetsov.bank.Server $@
