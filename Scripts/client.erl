-module(client).

-export([start/2]).

start(NameProxy,URL) ->
	{proxy,NameProxy} ! {self(),"solr/select",URL},
	receive
		{_pid,Data} ->io:format("~s", [Data])
	end.
	
