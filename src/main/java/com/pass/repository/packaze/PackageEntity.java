package com.pass.repository.packaze;

import com.pass.repository.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@Table(name = "package")
public class PackageEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer packageSeq;

    private String packageName;
    private Integer count;
    private Integer period;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PackageEntity)) return false;
        PackageEntity that = (PackageEntity) o;
        return Objects.equals(getPackageSeq(), that.getPackageSeq()) && Objects.equals(getPackageName(), that.getPackageName()) && Objects.equals(getCount(), that.getCount()) && Objects.equals(getPeriod(), that.getPeriod());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPackageSeq(), getPackageName(), getCount(), getPeriod());
    }
}

