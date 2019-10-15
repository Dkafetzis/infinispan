package org.infinispan.marshall.protostream.impl;

import org.infinispan.protostream.BaseMarshaller;
import org.infinispan.protostream.FileDescriptorSource;
import org.infinispan.protostream.SerializationContext;
import org.infinispan.protostream.SerializationContextInitializer;
import org.infinispan.protostream.config.Configuration;

/**
 * Manages {@link SerializationContext} across modules for use by various components.
 *
 * @author Ryan Emerson
 * @since 10.0
 */
public interface SerializationContextRegistry {

   void addProtoFile(MarshallerType type, FileDescriptorSource fileDescriptorSource);

   void addMarshaller(MarshallerType type, BaseMarshaller marshaller);

   void addContextInitializer(MarshallerType type, SerializationContextInitializer sci);

   void addConfiguration(MarshallerType type, Configuration config);

   SerializationContext getGlobalCtx();

   SerializationContext getPersistenceCtx();

   enum MarshallerType {
      GLOBAL,
      PERSISTENCE,
   }
}
