package com.noteapp.adapter.in.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class ViewController {

    @GetMapping({"/", "/login"})
    public String index() {
        return "index";
    }
    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }
    @GetMapping("/edit-profile")
    public String edit_profile() {
        return "edit-profile";
    }
    @GetMapping("/note-detail/{id}")  // PathVariable을 사용하여 노트 ID를 받는 매핑 추가
    public String noteDetail(@PathVariable String id) {
        return "note-detail";  // note-detail.html로 연결
    }
    @GetMapping("/create-note")
    public String createNote() {
        return "create-note";
    }
}
