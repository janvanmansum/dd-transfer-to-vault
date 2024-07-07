/*
 * Copyright (C) 2021 DANS - Data Archiving and Networked Services (info@dans.knaw.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.knaw.dans.ttv.client;

import nl.knaw.dans.ttv.core.TransferItem;
import nl.knaw.dans.ttv.core.domain.FileContentAttributes;

import java.io.IOException;

/**
 * Client for the Vault Catalog API.
 */
public interface VaultCatalogClient {
    void registerOcflObjectVersion(TransferItem transferItem) throws IOException;

    void registerOcflObjectVersion(FileContentAttributes fileContentAttributes) throws IOException;
}
