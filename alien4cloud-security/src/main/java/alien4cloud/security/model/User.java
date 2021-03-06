package alien4cloud.security.model;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

import org.elasticsearch.annotation.BooleanField;
import org.elasticsearch.annotation.ESObject;
import org.elasticsearch.annotation.Id;
import org.elasticsearch.annotation.StringField;
import org.elasticsearch.annotation.query.TermFilter;
import org.elasticsearch.annotation.query.TermsFacet;
import org.elasticsearch.mapping.IndexType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.social.security.SocialUserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.collect.Sets;

@ESObject
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class User implements SocialUserDetails {
    private static final long serialVersionUID = 1L;
    @Id
    private String username;
    private String password;
    private String lastName;
    private String firstName;
    @StringField(includeInAll = false, indexType = IndexType.no)
    private String email;
    @StringField(indexType = IndexType.not_analyzed)
    @TermsFacet
    private Set<String> groups;

    private Set<String> groupRoles;

    @TermFilter
    private String[] roles;
    @BooleanField(includeInAll = false, index = IndexType.no)
    private boolean internalDirectory;
    @BooleanField(includeInAll = false, index = IndexType.no)
    private boolean accountNonExpired = true;
    @BooleanField(includeInAll = false, index = IndexType.no)
    private boolean accountNonLocked = true;
    @BooleanField(includeInAll = false, index = IndexType.no)
    private boolean credentialsNonExpired = true;
    @BooleanField(includeInAll = false, index = IndexType.no)
    private boolean enabled = true;

    @JsonIgnore
    @Override
    public Set<SimpleGrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = Sets.newHashSet();
        if (roles != null) {
            for (String role : roles) {
                authorities.add(new SimpleGrantedAuthority(role));
            }
        }
        if (groupRoles != null) {
            for (String role : groupRoles) {
                authorities.add(new SimpleGrantedAuthority(role));
            }
        }
        return authorities;
    }

    @JsonIgnore
    @Override
    public String getUserId() {
        return this.username;
    }
}
