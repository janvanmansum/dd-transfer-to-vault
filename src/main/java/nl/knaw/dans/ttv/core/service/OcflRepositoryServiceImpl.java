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
package nl.knaw.dans.ttv.core.service;

import edu.wisc.library.ocfl.api.OcflOption;
import edu.wisc.library.ocfl.api.OcflRepository;
import edu.wisc.library.ocfl.api.model.ObjectVersionId;
import edu.wisc.library.ocfl.api.model.VersionInfo;
import nl.knaw.dans.ttv.db.TransferItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

public class OcflRepositoryServiceImpl implements OcflRepositoryService {
    private static final Logger log = LoggerFactory.getLogger(OcflRepositoryServiceImpl.class);

    private final FileService fileService;
    private final OcflRepositoryFactory ocflRepositoryFactory;

    public OcflRepositoryServiceImpl(FileService fileService, OcflRepositoryFactory ocflRepositoryFactory) {
        this.fileService = fileService;
        this.ocflRepositoryFactory = ocflRepositoryFactory;
    }

    @Override
    public OcflRepository createRepository(Path path, String id) throws IOException {
        // TODO: use sub-directories dve and ocfl-repo
        var newPath = fileService.createDirectory(Path.of(path.toString(), id));
        var newPathWorkdir = fileService.createDirectory(Path.of(path.toString(), id + "-wd"));

        log.trace("Creating OCFL repository on location '{}' and working dir '{}'", newPath, newPathWorkdir);
        return ocflRepositoryFactory.createRepository(newPath, newPathWorkdir);
    }

    @Override
    public OcflRepository openRepository(Path path, String id) {
        var newPath = Path.of(path.toString(), id);
        var newPathWorkdir = Path.of(path.toString(), id + "-wd");

        try {
            fileService.createDirectory(newPath);
        }
        catch (IOException e) {
            log.debug("Creating directory failed because repository already exists");
        }

        try {
            fileService.createDirectory(newPathWorkdir);
        }
        catch (IOException e) {
            log.debug("Creating directory failed because repository already exists");
        }

        log.trace("Opening OCFL repository on location '{}' and working dir '{}'", newPath, newPathWorkdir);
        return ocflRepositoryFactory.createRepository(newPath, newPathWorkdir);
    }

    @Override
    public String importTransferItem(OcflRepository ocflRepository, TransferItem transferItem) {
        var objectId = getObjectIdForTransferItem(transferItem);
        var source = Objects.requireNonNull(Path.of(transferItem.getDveFilePath()), "dveFilePath can't be null: " + transferItem.getDveFilePath());

        log.debug("Importing file '{}' with objectId '{}' into OCFL repository", source, objectId);
        ocflRepository.putObject(ObjectVersionId.head(objectId), source, new VersionInfo().setMessage("initial commit"), OcflOption.MOVE_SOURCE);

        return objectId;
    }

    @Override
    public String getObjectIdForTransferItem(TransferItem transferItem) {
        var bagId = Objects.requireNonNull(transferItem.getBagId(), "Bag ID can't be null: " + transferItem.getDveFilePath());
        return bagId.substring(0, 9) + bagId.substring(9, 11) + "/" + bagId.substring(11, 13) + "/" + bagId.substring(13, 15) + "/" + bagId.substring(15);
    }

    @Override
    public void closeOcflRepository(OcflRepository ocflRepository) {
        ocflRepository.close();
    }

    @Override
    public void cleanupRepository(Path path, String id) throws IOException {
        // does the inverse of createRepository
        var newPath = Path.of(path.toString(), id);
        var newPathWorkdir = Path.of(path.toString(), id + "-wd");

        log.debug("Deleting OCFL repository '{}'", newPath);
        fileService.deleteDirectory(newPath);

        log.debug("Deleting OCFL repository workdir '{}'", newPathWorkdir);
        fileService.deleteDirectory(newPathWorkdir);
    }
}
