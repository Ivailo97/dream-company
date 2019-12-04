package dreamcompany.util;

import java.util.List;

public interface MappingConverter {

    <M, D> D convert(M model, Class<D> destinationClass);

    <M, D> List<D> convertCollection(List<M> collection, Class<D> destinationClass);

    <M, D> D map(M model, Class<D> destinationClass);
}
