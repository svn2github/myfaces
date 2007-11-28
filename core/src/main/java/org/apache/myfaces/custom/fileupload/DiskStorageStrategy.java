package org.apache.myfaces.custom.fileupload;

import java.io.File;

public abstract class DiskStorageStrategy extends StorageStrategy {

  public abstract File getTempFile();

}
