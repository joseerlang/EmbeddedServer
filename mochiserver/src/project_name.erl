%% @author Mochi Media <dev@mochimedia.com>
%% @copyright 2010 Mochi Media <dev@mochimedia.com>

%% @doc project_name.

-module(project_name).
-author("Mochi Media <dev@mochimedia.com>").
-export([start/0, stop/0]).

ensure_started(App) ->
    case application:start(App) of
        ok ->
            ok;
        {error, {already_started, App}} ->
            ok
    end.


%% @spec start() -> ok
%% @doc Start the project_name server.
start() ->
    project_name_deps:ensure(),
    ensure_started(crypto),
    application:start(project_name).


%% @spec stop() -> ok
%% @doc Stop the project_name server.
stop() ->
    application:stop(project_name).
