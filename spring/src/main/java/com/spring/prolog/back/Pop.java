package com.spring.prolog.back;

import javax.swing.JOptionPane;

public class Pop {
	public void Up(int num){
		if(num == 0){
			JOptionPane.showMessageDialog(null, "이미 가입된 이메일입니다.");
		}
		else if(num == 1){
			JOptionPane.showMessageDialog(null, "회원가입이 완료되었습니다.");
		}
		else if(num == 2){
			JOptionPane.showMessageDialog(null, "아이디 혹은 비밀번호를 잘못 입력하셨습니다.");
		}
	}
}
