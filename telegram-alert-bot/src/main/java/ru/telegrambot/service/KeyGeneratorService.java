package ru.telegrambot.service;

import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KeyGeneratorService {

    private final PasswordGenerator generator = new PasswordGenerator();

    CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
    CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);

    CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
    CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);

    CharacterData digitChars = EnglishCharacterData.Digit;
    CharacterRule digitRule = new CharacterRule(digitChars);

    CharacterData specialChars = new CharacterData() {
        @Override
        public String getErrorCode() {
            return "ERROR";
        }

        @Override
        public String getCharacters() {
            return "!@#$%^&*()_+";
        }
    };
    CharacterRule splCharRule = new CharacterRule(specialChars);

    @PostConstruct
    public void init() {
        lowerCaseRule.setNumberOfCharacters(2);
        upperCaseRule.setNumberOfCharacters(2);
        digitRule.setNumberOfCharacters(2);
        splCharRule.setNumberOfCharacters(2);

    }

    public String getNewTelegramRandomKey() {
        String key = generator.generatePassword(128, splCharRule, lowerCaseRule,
                upperCaseRule, digitRule);
        log.info(key);
        return key;
    }


}
