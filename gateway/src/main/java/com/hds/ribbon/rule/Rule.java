package com.hds.ribbon.rule;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;

import java.util.ArrayList;
import java.util.List;

public class Rule extends AbstractLoadBalancerRule {
    private volatile int total;
    private volatile int index;

    List<Server> upServers = new ArrayList<>();

    public Server choose(ILoadBalancer lb, Object o) {
        if (lb == null) {
            return null;
        } else {
            Server server = null;
            while (server == null) {
                if (Thread.interrupted()) {
                    return null;
                }
                List<Server> servers = lb.getAllServers();
                int serverCnt = servers.size();
                if (serverCnt == 0) {
                    return null;
                }
                if (total == 0) {
                    upServers = lb.getReachableServers();
                }
                if (total < 3) {
                    if (upServers.size() != lb.getReachableServers().size()) {
                        index = 0;
                    }
                    server = lb.getReachableServers().get(index);
                    total++;
                } else {
                    total = 0;
                    index++;
                    if (index >= lb.getReachableServers().size())
                    {
                        index = 0;
                    }
                }
                if (server == null)
                {
                    Thread.yield();
                }
                else
                {
                    if (server.isAlive())
                    {
                        return server;
                    }

                    server = null;
                    Thread.yield();
                }
            }
            return server;
        }
    }

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {
    }

    @Override
    public Server choose(Object o) {
        return this.choose(this.getLoadBalancer(), o);
    }
}
