package org.infinispan.multimap.impl.function.set;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import org.infinispan.commons.marshall.AdvancedExternalizer;
import org.infinispan.functional.EntryView;
import org.infinispan.multimap.impl.ExternalizerIds;
import org.infinispan.multimap.impl.SetBucket;

/**
 * Serializable function used by
 * {@link org.infinispan.multimap.impl.EmbeddedSetCache#add}
 * to add elements to a Set.
 *
 * @author Vittorio Rigamonti
 * @see <a href="http://infinispan.org/documentation/">Marshalling of Functions</a>
 * @since 15.0
 */
public final class SAddFunction<K, V> implements SetBucketBaseFunction<K, V, Boolean> {
   public static final AdvancedExternalizer<SAddFunction> EXTERNALIZER = new Externalizer();
   private final V value;

   public SAddFunction(V value) {
      this.value = value;
   }

   @Override
   public Boolean apply(EntryView.ReadWriteEntryView<K, SetBucket<V>> entryView) {
      Optional<SetBucket<V>> existing = entryView.peek();
      boolean retVal;
      if (existing.isPresent()) {
         var s = existing.get();
         retVal = existing.get().add(value);
         //don't change the cache if the value already exists. it avoids replicating a no-op
         if (retVal) {
            entryView.set(s);
         }
      } else {
         entryView.set(SetBucket.create(value));
         retVal = true;
      }
      return retVal;
   }

   private static class Externalizer implements AdvancedExternalizer<SAddFunction> {

      @Override
      public Set<Class<? extends SAddFunction>> getTypeClasses() {
         return Collections.singleton(SAddFunction.class);
      }

      @Override
      public Integer getId() {
         return ExternalizerIds.SET_ADD_FUNCTION;
      }

      @Override
      public void writeObject(ObjectOutput output, SAddFunction object) throws IOException {
         output.writeObject(object.value);
      }

      @Override
      public SAddFunction readObject(ObjectInput input) throws IOException, ClassNotFoundException {
         return new SAddFunction(input.readObject());
      }
   }
}