package kd.push.utils;

import java.io.UnsupportedEncodingException;

public class Base64 {
    static final /* synthetic */ boolean $assertionsDisabled = (!Base64.class.desiredAssertionStatus());
    public static final int CRLF = 4;
    public static final int DEFAULT = 0;
    public static final int NO_CLOSE = 16;
    public static final int NO_PADDING = 1;
    public static final int NO_WRAP = 2;
    public static final int URL_SAFE = 8;

    static abstract class Coder {
        public int op;
        public byte[] output;

        public abstract int maxOutputSize(int i);

        public abstract boolean process(byte[] bArr, int i, int i2, boolean z);

        Coder() {
        }
    }

    static class Decoder extends Coder {
        private static final int[] DECODE = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        private static final int[] DECODE_WEBSAFE = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        private static final int EQUALS = -2;
        private static final int SKIP = -1;
        private final int[] alphabet;
        private int state;
        private int value;

        public Decoder(int flags, byte[] output) {
            this.output = output;
            this.alphabet = (flags & 8) == 0 ? DECODE : DECODE_WEBSAFE;
            this.state = 0;
            this.value = 0;
        }

        public int maxOutputSize(int len) {
            return ((len * 3) / 4) + 10;
        }

        public boolean process(byte[] input, int offset, int len, boolean finish) {
            if (this.state == 6) {
                return false;
            }
            int op;
            int p = offset;
            len += offset;
            int state = this.state;
            int value = this.value;
            int op2 = 0;
            byte[] output = this.output;
            int[] alphabet = this.alphabet;
            while (p < len) {
                if (state == 0) {
                    while (p + 4 <= len) {
                        value = (((alphabet[input[p] & 255] << 18) | (alphabet[input[p + 1] & 255] << 12)) | (alphabet[input[p + 2] & 255] << 6)) | alphabet[input[p + 3] & 255];
                        if (value >= 0) {
                            output[op2 + 2] = (byte) value;
                            output[op2 + 1] = (byte) (value >> 8);
                            output[op2] = (byte) (value >> 16);
                            op2 += 3;
                            p += 4;
                        } else if (p >= len) {
                            op = op2;
                            if (finish) {
                                switch (state) {
                                    case 0:
                                        op2 = op;
                                        break;
                                    case 1:
                                        this.state = 6;
                                        return false;
                                    case 2:
                                        op2 = op + 1;
                                        output[op] = (byte) (value >> 4);
                                        break;
                                    case 3:
                                        op2 = op + 1;
                                        output[op] = (byte) (value >> 10);
                                        op = op2 + 1;
                                        output[op2] = (byte) (value >> 2);
                                        op2 = op;
                                        break;
                                    case 4:
                                        this.state = 6;
                                        return false;
                                    default:
                                        op2 = op;
                                        break;
                                }
                                this.state = state;
                                this.op = op2;
                                return true;
                            }
                            this.state = state;
                            this.value = value;
                            this.op = op;
                            return true;
                        }
                    }
                    if (p >= len) {
                        op = op2;
                        if (finish) {
                            switch (state) {
                                case 0:
                                    op2 = op;
                                    break;
                                case 1:
                                    this.state = 6;
                                    return false;
                                case 2:
                                    op2 = op + 1;
                                    output[op] = (byte) (value >> 4);
                                    break;
                                case 3:
                                    op2 = op + 1;
                                    output[op] = (byte) (value >> 10);
                                    op = op2 + 1;
                                    output[op2] = (byte) (value >> 2);
                                    op2 = op;
                                    break;
                                case 4:
                                    this.state = 6;
                                    return false;
                                default:
                                    op2 = op;
                                    break;
                            }
                            this.state = state;
                            this.op = op2;
                            return true;
                        }
                        this.state = state;
                        this.value = value;
                        this.op = op;
                        return true;
                    }
                }
                int p2 = p + 1;
                int d = alphabet[input[p] & 255];
                switch (state) {
                    case 0:
                        if (d < 0) {
                            if (d == -1) {
                                break;
                            }
                            this.state = 6;
                            return false;
                        }
                        value = d;
                        state++;
                        break;
                    case 1:
                        if (d < 0) {
                            if (d == -1) {
                                break;
                            }
                            this.state = 6;
                            return false;
                        }
                        value = (value << 6) | d;
                        state++;
                        break;
                    case 2:
                        if (d < 0) {
                            if (d != -2) {
                                if (d == -1) {
                                    break;
                                }
                                this.state = 6;
                                return false;
                            }
                            op = op2 + 1;
                            output[op2] = (byte) (value >> 4);
                            state = 4;
                            op2 = op;
                            break;
                        }
                        value = (value << 6) | d;
                        state++;
                        break;
                    case 3:
                        if (d < 0) {
                            if (d != -2) {
                                if (d == -1) {
                                    break;
                                }
                                this.state = 6;
                                return false;
                            }
                            output[op2 + 1] = (byte) (value >> 2);
                            output[op2] = (byte) (value >> 10);
                            op2 += 2;
                            state = 5;
                            break;
                        }
                        value = (value << 6) | d;
                        output[op2 + 2] = (byte) value;
                        output[op2 + 1] = (byte) (value >> 8);
                        output[op2] = (byte) (value >> 16);
                        op2 += 3;
                        state = 0;
                        break;
                    case 4:
                        if (d != -2) {
                            if (d == -1) {
                                break;
                            }
                            this.state = 6;
                            return false;
                        }
                        state++;
                        break;
                    case 5:
                        if (d == -1) {
                            break;
                        }
                        this.state = 6;
                        return false;
                    default:
                        break;
                }
                p = p2;
            }
            op = op2;
            if (finish) {
                this.state = state;
                this.value = value;
                this.op = op;
                return true;
            }
            switch (state) {
                case 0:
                    op2 = op;
                    break;
                case 1:
                    this.state = 6;
                    return false;
                case 2:
                    op2 = op + 1;
                    output[op] = (byte) (value >> 4);
                    break;
                case 3:
                    op2 = op + 1;
                    output[op] = (byte) (value >> 10);
                    op = op2 + 1;
                    output[op2] = (byte) (value >> 2);
                    op2 = op;
                    break;
                case 4:
                    this.state = 6;
                    return false;
                default:
                    op2 = op;
                    break;
            }
            this.state = state;
            this.op = op2;
            return true;
        }
    }

    static class Encoder extends Coder {
        static final /* synthetic */ boolean $assertionsDisabled;
        private static final byte[] ENCODE = new byte[]{(byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, (byte) 79, (byte) 80, (byte) 81, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, (byte) 90, (byte) 97, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102, (byte) 103, (byte) 104, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, (byte) 110, (byte) 111, (byte) 112, (byte) 113, (byte) 114, (byte) 115, (byte) 116, (byte) 117, (byte) 118, (byte) 119, (byte) 120, (byte) 121, (byte) 122, (byte) 48, (byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 43, (byte) 47};
        private static final byte[] ENCODE_WEBSAFE = new byte[]{(byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, (byte) 79, (byte) 80, (byte) 81, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, (byte) 90, (byte) 97, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102, (byte) 103, (byte) 104, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, (byte) 110, (byte) 111, (byte) 112, (byte) 113, (byte) 114, (byte) 115, (byte) 116, (byte) 117, (byte) 118, (byte) 119, (byte) 120, (byte) 121, (byte) 122, (byte) 48, (byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 45, (byte) 95};
        public static final int LINE_GROUPS = 19;
        private final byte[] alphabet;
        private int count;
        public final boolean do_cr;
        public final boolean do_newline;
        public final boolean do_padding;
        private final byte[] tail;
        int tailLen;

        static {
            boolean z;
            if (Base64.class.desiredAssertionStatus()) {
                z = false;
            } else {
                z = true;
            }
            $assertionsDisabled = z;
        }

        public Encoder(int flags, byte[] output) {
            boolean z;
            boolean z2 = true;
            this.output = output;
            this.do_padding = (flags & 1) == 0;
            if ((flags & 2) == 0) {
                z = true;
            } else {
                z = false;
            }
            this.do_newline = z;
            if ((flags & 4) == 0) {
                z2 = false;
            }
            this.do_cr = z2;
            this.alphabet = (flags & 8) == 0 ? ENCODE : ENCODE_WEBSAFE;
            this.tail = new byte[2];
            this.tailLen = 0;
            this.count = this.do_newline ? 19 : -1;
        }

        public int maxOutputSize(int len) {
            return ((len * 8) / 5) + 10;
        }

        public boolean process(byte[] r15, int r16, int r17, boolean r18) {
            throw new UnsupportedOperationException("Method not decompiled: kd.push.utils.Base64.Encoder.process(byte[], int, int, boolean):boolean");
        }
    }

    public static byte[] decode(String str, int flags) {
        return decode(str.getBytes(), flags);
    }

    public static byte[] decode(byte[] input, int flags) {
        return decode(input, 0, input.length, flags);
    }

    public static byte[] decode(byte[] input, int offset, int len, int flags) {
        Decoder decoder = new Decoder(flags, new byte[((len * 3) / 4)]);
        if (!decoder.process(input, offset, len, true)) {
            throw new IllegalArgumentException("bad base-64");
        } else if (decoder.op == decoder.output.length) {
            return decoder.output;
        } else {
            byte[] temp = new byte[decoder.op];
            System.arraycopy(decoder.output, 0, temp, 0, decoder.op);
            return temp;
        }
    }

    public static String encodeToString(byte[] input, int flags) {
        try {
            return new String(encode(input, flags), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    public static String encodeToString(byte[] input, int offset, int len, int flags) {
        try {
            return new String(encode(input, offset, len, flags), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    public static byte[] encode(byte[] input, int flags) {
        return encode(input, 0, input.length, flags);
    }

    public static byte[] encode(byte[] input, int offset, int len, int flags) {
        Encoder encoder = new Encoder(flags, null);
        int output_len = (len / 3) * 4;
        if (!encoder.do_padding) {
            switch (len % 3) {
                case 0:
                    break;
                case 1:
                    output_len += 2;
                    break;
                case 2:
                    output_len += 3;
                    break;
                default:
                    break;
            }
        } else if (len % 3 > 0) {
            output_len += 4;
        }
        if (encoder.do_newline && len > 0) {
            int i;
            int i2 = ((len - 1) / 57) + 1;
            if (encoder.do_cr) {
                i = 2;
            } else {
                i = 1;
            }
            output_len += i * i2;
        }
        encoder.output = new byte[output_len];
        encoder.process(input, offset, len, true);
        if ($assertionsDisabled || encoder.op == output_len) {
            return encoder.output;
        }
        throw new AssertionError();
    }

    private Base64() {
    }
}
