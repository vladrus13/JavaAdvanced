#!/bin/bash
cd ../../../../../ &&
java -cp .:"ru/ifmo/rain/kuznetsov/bank" ru.ifmo.rain.kuznetsov.bank.client.Client $@
