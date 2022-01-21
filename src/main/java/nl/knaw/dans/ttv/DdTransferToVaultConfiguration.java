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

package nl.knaw.dans.ttv;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import nl.knaw.dans.lib.util.ExecutorServiceFactory;
import nl.knaw.dans.ttv.config.CollectConfiguration;
import nl.knaw.dans.ttv.config.ConfirmArchivedConfiguration;
import nl.knaw.dans.ttv.config.CreateOcflConfiguration;
import nl.knaw.dans.ttv.config.CreateTarConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DdTransferToVaultConfiguration extends Configuration {

    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    @Valid
    @NotNull
    private CollectConfiguration collect;

    @Valid
    @NotNull
    private CreateOcflConfiguration createOcfl;

    @Valid
    @NotNull
    private CreateTarConfiguration createTar;

    @Valid
    @NotNull
    private ConfirmArchivedConfiguration confirmArchived;

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    @JsonProperty("database")
    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        this.database = dataSourceFactory;
    }

    public CollectConfiguration getCollect() {
        return collect;
    }

    public CreateOcflConfiguration getCreateOcfl() {
        return createOcfl;
    }

    public CreateTarConfiguration getCreateTar() {
        return createTar;
    }

    public ConfirmArchivedConfiguration getConfirmArchived() {
        return confirmArchived;
    }
}
