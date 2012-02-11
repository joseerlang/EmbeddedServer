#!/bin/sh
# NOTE: mustache templates need \ because they are not awesome.
exec erl -pa ebin edit deps/*/ebin -boot start_sasl \
    -sname project_name_dev \
    -s project_name \
    -setcookie cookie \
    -s reloader
