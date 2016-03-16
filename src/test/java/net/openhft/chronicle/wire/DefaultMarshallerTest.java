package net.openhft.chronicle.wire;

import net.openhft.chronicle.bytes.Bytes;
import net.openhft.chronicle.core.util.ObjectUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by peter on 16/03/16.
 */
public class DefaultMarshallerTest {
    @Test
    public void testDeserialize() {
        DMOuterClass dmOuterClass = ObjectUtils.newInstance(DMOuterClass.class);
        assertNotNull(dmOuterClass.nested);

        DMOuterClass oc = new DMOuterClass("words", true, (byte) 1, 2, 3, 4, 5, (short) 6);
        oc.nested.add(new DMNestedClass("hi", 111));
        oc.nested.add(new DMNestedClass("bye", 999));
        oc.map.put("key", new DMNestedClass("value", 1));
        oc.map.put("keyz", new DMNestedClass("valuez", 1111));

        assertEquals("!net.openhft.chronicle.wire.DefaultMarshallerTest$DMOuterClass {\n" +
                "  text: words, b: true, bb: 1, s: 6, f: 3.0, d: 2.0, l: 5, i: 4, nested: [\n" +
                "    { str: hi, num: 111 },\n" +
                "      {\n" +
                "      str: bye, num: 999 }\n" +
                "    ]\n" +
                "    map: !!seqmap [\n" +
                "      { key: key,\n" +
                "        value: !net.openhft.chronicle.wire.DefaultMarshallerTest$DMNestedClass { str: value, num: 1 } },\n" +
                "      { key: keyz,\n" +
                "        value: !net.openhft.chronicle.wire.DefaultMarshallerTest$DMNestedClass { str: valuez, num: 1111 } }\n" +
                "    ]\n" +
                "    }", oc.toString());

        Wire text = new TextWire(Bytes.elasticByteBuffer());
        oc.writeMarshallable(text);

        DMOuterClass oc2 = new DMOuterClass();
        oc2.readMarshallable(text);

        assertEquals(oc, oc2);
    }

    static class DMOuterClass extends AbstractMarshallable {
        String text;
        boolean b;
        byte bb;
        short s;
        float f;
        double d;
        long l;
        int i;
        List<DMNestedClass> nested = new ArrayList<>();
        Map<String, DMNestedClass> map = new LinkedHashMap<>();

        DMOuterClass() {

        }

        public DMOuterClass(String text, boolean b, byte bb, double d, float f, int i, long l, short s) {
            this.text = text;
            this.b = b;
            this.bb = bb;
            this.d = d;
            this.f = f;
            this.i = i;
            this.l = l;
            this.s = s;
        }
    }

    static class DMNestedClass implements Marshallable {
        String str;
        int num;

        public DMNestedClass(String str, int num) {
            this.str = str;
            this.num = num;
        }
    }
}
