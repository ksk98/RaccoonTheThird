package com.bots.RaccoonServer.ChecksumCalculation;

import com.bots.RaccoonServer.Utility.Tools;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

public class ChecksumCalculationTests {

    @Test
    public void testChecksumCalculation() {
        ChecksumTestClass obj = new ChecksumTestClass();
        obj.name = "name";
        obj.age = 1;

        List<String> checksum = new LinkedList<>();

        try {
            if (failIfNotDistinct(checksum, Tools.getChecksumForObject(obj))) return;
            obj.name = "enam";
            if (failIfNotDistinct(checksum, Tools.getChecksumForObject(obj))) return;
            obj.age = 2;
            if (failIfNotDistinct(checksum, Tools.getChecksumForObject(obj))) return;
            obj.name = "name";
            if (failIfNotDistinct(checksum, Tools.getChecksumForObject(obj))) return;

            if (!checkIfEqualsOrFail(checksum.get(3), obj)) return;
            obj.name = "enam";
            if (!checkIfEqualsOrFail(checksum.get(2), obj)) return;
            obj.age = 1;
            if (!checkIfEqualsOrFail(checksum.get(1), obj)) return;
            obj.name = "name";
            checkIfEqualsOrFail(checksum.get(0), obj);
        } catch (NoSuchAlgorithmException | IOException e) {
            Assertions.fail(e.toString());
        }
    }

    private boolean failIfNotDistinct(List<String> list, String checksum) {
        if (list.contains(checksum)) {
            Assertions.fail();
            return true;
        }

        list.add(checksum);
        return false;
    }

    private boolean checkIfEqualsOrFail(String checksum, Serializable object) throws IOException, NoSuchAlgorithmException {
        if (checksum.equals(Tools.getChecksumForObject(object)))
            return true;

        Assertions.fail();
        return false;
    }
}
