FROM docker.io/rustembedded/cross:arm-unknown-linux-gnueabihf

COPY openssl.sh /
RUN bash /openssl.sh linux-generic32 arm-linux-gnueabihf-

ENV OPENSSL_DIR=/openssl \
    OPENSSL_INCLUDE_DIR=/openssl/include \
    OPENSSL_LIB_DIR=/openssl/lib \