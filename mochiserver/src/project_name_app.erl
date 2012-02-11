%% @author Mochi Media <dev@mochimedia.com>
%% @copyright project_name Mochi Media <dev@mochimedia.com>

%% @doc Callbacks for the project_name application.

-module(project_name_app).
-author("Mochi Media <dev@mochimedia.com>").

-behaviour(application).
-export([start/2,stop/1]).


%% @spec start(_Type, _StartArgs) -> ServerRet
%% @doc application start callback for project_name.
start(_Type, _StartArgs) ->
    project_name_deps:ensure(),
    project_name_sup:start_link().

%% @spec stop(_State) -> ServerRet
%% @doc application stop callback for project_name.
stop(_State) ->
    ok.
