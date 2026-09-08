package com.ecotrack.domain;

import java.util.Optional;

@FunctionalInterface
public interface AssetLookup {

    Optional<ITAsset> findById(long assetId);
}