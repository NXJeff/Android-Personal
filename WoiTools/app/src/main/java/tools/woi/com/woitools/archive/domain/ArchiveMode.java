package tools.woi.com.woitools.archive.domain;

/**
 * Created by YeekFeiTan on 12/29/2015.
 */
public enum ArchiveMode {

    OlderThan, MoreThan;

    @Override
    public String toString() {
        switch (this) {
            case MoreThan:
                return "More Than";
            case OlderThan:
                return "File older than";
        }

        return null;
    }
}
