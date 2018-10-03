package content.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.collections4.KeyValue;

import content.util.FileManager;

@Entity
@NamedQueries({ @NamedQuery(name = "FileContent.findAll", query = "select o from FileContent o"),
                @NamedQuery(name = "FileContent.findByPath", query = "select o from FileContent o WHERE o.folder = :folder"),
                @NamedQuery(name = "FileContent.findByType",
                            query = "select o from FileContent o WHERE o.folder = :folder AND o.type like :type"),
                @NamedQuery(name = "FileContent.findByFullPath",
                			query = "select o from FileContent o WHERE o.name = :name AND o.folder = :folder"),
                @NamedQuery(name = "FileContent.findByGuid",
                			query = "select o from FileContent o WHERE o.guid = :guid")
    })
@Table(name = "FILE_CONTENT", schema = "EM_CONTENT")
public class FileContent
    implements Serializable {
    @SuppressWarnings("compatibility:-6036960598959135359")
    private static final long serialVersionUID = 4934379702205247322L;
    @Temporal(TemporalType.DATE)
    @Column(name = "CREATE_DATE")
    private Date createDate;
    @Column(name = "CREATE_USER", length = 30)
    private String createUser;
    @Column(name = "STORED_FILENAME", length = 30)
    private String storedFileName;
    @Column(name = "FILE_SIZE")
    private BigDecimal fileSize;
    @Column(nullable = false, length = 4000)
    private String folder;
    @Id
    @Column(nullable = false)
    private String guid;
    @Column(nullable = false, length = 1000)
    private String name;
    @Column(name = "SORT_ORDER")
    private Long sortOrder;
    @Column(length = 250)
    private String type;

    @Column(name = "UPDATE_DATE")
    private Timestamp updateDate;
    @Column(name = "UPDATE_USER", length = 30)
    private String updateUser;

    public FileContent() {
    }

    public FileContent(Date createDate, String createUser, BigDecimal fileSize, String folder, String name, Long sortOrder, String type,
                           Timestamp updateDate, String updateUser) {
        this.createDate = createDate;
        this.createUser = createUser;
        this.fileSize = fileSize;
        this.folder = folder;
        this.name = name;
        this.sortOrder = sortOrder;
        this.type = type;
        this.updateDate = updateDate;
        this.updateUser = updateUser;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public BigDecimal getFileSize() {
        return fileSize;
    }

    public void setFileSize(BigDecimal fileSize) {
        this.fileSize = fileSize;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Long sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName() + "@" + Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("createDate=");
        buffer.append(getCreateDate());
        buffer.append(',');
        buffer.append("createUser=");
        buffer.append(getCreateUser());
        buffer.append(',');
        buffer.append("fileSize=");
        buffer.append(getFileSize());
        buffer.append(',');
        buffer.append("folder=");
        buffer.append(getFolder());
        buffer.append(',');
        buffer.append("guid=");
        buffer.append(getGuid());
        buffer.append(',');
        buffer.append("name=");
        buffer.append(getName());
        buffer.append(',');
        buffer.append("sortOrder=");
        buffer.append(getSortOrder());
        buffer.append(',');
        buffer.append("type=");
        buffer.append(getType());
        buffer.append(',');
        buffer.append("updateDate=");
        buffer.append(getUpdateDate());
        buffer.append(',');
        buffer.append("updateUser=");
        buffer.append(getUpdateUser());
        buffer.append(']');
        return buffer.toString();
    }

    public String getStoredFileName() {
        return storedFileName;
    }

    public void setStoredFileName(String storedFileName) {
        this.storedFileName = storedFileName;
    }

    public String getStorageFullPath() {
        return FileManager.createFilePathFromRoot(folder, storedFileName);
    }

    public KeyValue splitFullPath(String fullPath) {
        return FileManager.splitPath(fullPath);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + Objects.hashCode(guid);
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(folder);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final FileContent bean = (FileContent)o;
        if (!Objects.equals(guid, bean.getGuid()) || !Objects.equals(folder, bean.getFolder())) {
            return false;
        }

        return Objects.equals(name, bean.getName());
    }
}
