package xyz.zlatanov.subsbuddy.controller;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;

import java.io.IOException;
import java.util.Objects;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import xyz.zlatanov.subsbuddy.service.HomeService;
import xyz.zlatanov.subsbuddy.util.WebUtils;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class HomeController {

	private HomeService service;

	@GetMapping
	public String home(HttpServletRequest request, Model model,
			@RequestParam(value = "error", defaultValue = "#{null}") String error,
			@RequestParam(value = "message", defaultValue = "#{null}") String message) {
		model.addAttribute("model", service.getModel(WebUtils.getOwner(request)));
		model.addAttribute("error", error);
		model.addAttribute("message", message);
		return "home";
	}

	@SneakyThrows
	@PostMapping
	public String upload(HttpServletRequest request, @RequestParam("srt") MultipartFile file) {
		service.upload(Objects.requireNonNull(file.getOriginalFilename()), file.getBytes(), WebUtils.getOwner(request));
		return WebUtils.getErrorQueryParamRedirect(request, "message", "Uploaded: " + file.getOriginalFilename());
	}

	@GetMapping("download")
	void download(@RequestParam("id") String id, HttpServletResponse response) throws IOException {
		val downloadModel = service.download(id);
		response.setContentType(APPLICATION_OCTET_STREAM_VALUE);
		response.setHeader(CONTENT_DISPOSITION, "attachment; filename=" + downloadModel.filename());
		response.getOutputStream().write(downloadModel.content());
		response.getOutputStream().close();
	}

	@GetMapping("delete")
	public String delete(HttpServletRequest request, @RequestParam("id") String id) {
		service.delete(id);
		return WebUtils.getErrorQueryParamRedirect(request, "message", "Successfully deleted.");
	}
}