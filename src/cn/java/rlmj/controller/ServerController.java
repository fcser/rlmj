package cn.java.rlmj.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.java.rlmj.connect.Path;
import cn.java.rlmj.connect.Server;
import cn.java.rlmj.service.PeopleService;

@Controller
public class ServerController {
	private static volatile Server server = null;
	private static volatile boolean start = false;

	@Resource
	private PeopleService peopleService;

	@RequestMapping("StartServer")
	public void startServer(HttpServletRequest request) {
		if (!start)
			synchronized (getClass()) {
				if (server == null) {
					Path.path=request.getSession().getServletContext().getRealPath("/upload");
					System.out.println(Path.path);
					server = new Server(peopleService);
					start = true;
				}
			}
		server.start();
	}
}
