Summary: Support package for spacewalk koan interaction
Name: spacewalk-koan
Version: 2.8.1
Release: 1%{?dist}
Group: System Environment/Kernel
License: GPLv2
Source0: https://github.com/spacewalkproject/spacewalk/archive/%{name}-%{version}.tar.gz
URL:            https://github.com/spacewalkproject/spacewalk
BuildRoot: %{_tmppath}/%{name}-%{version}-%{release}-buildroot
BuildArch:      noarch
BuildRequires:  python
Requires:       python >= 1.5
Requires:       koan >= 1.4.3
Requires:       xz
%if 0%{?suse_version}
# provide directories for filelist check in OBS
BuildRequires: rhn-client-tools
%endif
Conflicts: rhn-kickstart
Conflicts: rhn-kickstart-common
Conflicts: rhn-kickstart-virtualization

Requires: rhn-check

%description
Support package for spacewalk koan interaction.

%prep
%setup -q

%build
make -f Makefile.spacewalk-koan all

%install
rm -rf $RPM_BUILD_ROOT
make -f Makefile.spacewalk-koan install PREFIX=$RPM_BUILD_ROOT ROOT=%{_datadir}/rhn/ \
    MANDIR=%{_mandir}

%clean
rm -rf $RPM_BUILD_ROOT

%files
%config(noreplace)  %{_sysconfdir}/sysconfig/rhn/clientCaps.d/kickstart
%{_sbindir}/*
%{_datadir}/rhn/spacewalkkoan/
%{_datadir}/rhn/actions/

%changelog
* Wed Sep 06 2017 Michael Mraka <michael.mraka@redhat.com> 2.8.1-1
- purged changelog entries for Spacewalk 2.0 and older
- use standard brp-python-bytecompile
- Bumping package versions for 2.8.

* Tue Jul 18 2017 Michael Mraka <michael.mraka@redhat.com> 2.7.2-1
- move version and release before sources

* Mon Jul 17 2017 Jan Dobes 2.7.1-1
- Updated links to github in spec files
- Migrating Fedorahosted to GitHub
- Bumping package versions for 2.7.

* Mon Sep 26 2016 Jan Dobes 2.6.1-1
- embed_kickstart was renamed to embed_autoinst in koan upstream
- Bumping package versions for 2.6.

* Wed May 25 2016 Tomas Kasparek <tkasparek@redhat.com> 2.5.2-1
- updating copyright years

* Tue Apr 26 2016 Gennadii Altukhov <galt@redhat.com> 2.5.1-1
- Adapt spacewalk-koan for Python 2/3 compatibility
- Bumping package versions for 2.5.

* Wed Sep 16 2015 Jan Dobes 2.4.2-1
- 1253464 - switch to KVM if possible

* Fri May 29 2015 Jan Dobes 2.4.1-1
- fixing duplicate BuildArch
- Bumping package versions for 2.4.

* Thu Mar 19 2015 Grant Gainey 2.3.2-1
- Updating copyright info for 2015

* Fri Jan 30 2015 Stephen Herr <sherr@redhat.com> 2.3.1-1
- 1187482 - make file preservation work again with new upstream koan
- spacewalk-koan: improved merge-rd
- Bumping package versions for 2.3.

* Fri Jul 11 2014 Milan Zazrivec <mzazrivec@redhat.com> 2.2.4-1
- fix copyright years

* Fri Jun 13 2014 Stephen Herr <sherr@redhat.com> 2.2.3-1
- 1109276 - make cobbler20 guest kickstart work with new koan

* Wed Mar 26 2014 Stephen Herr <sherr@redhat.com> 2.2.2-1
- 1063409 - guest provisioned on RHEL 7 host have no graphical console
- Merge pull request #9 from dyordano/1071657

* Fri Mar 14 2014 Michael Mraka <michael.mraka@redhat.com> 2.2.1-1
- remove unneded imports

* Fri Dec 20 2013 Milan Zazrivec <mzazrivec@redhat.com> 2.1.4-1
- 967503 - use new Koan attribute

* Mon Oct 14 2013 Michael Mraka <michael.mraka@redhat.com> 2.1.3-1
- cleaning up old svn Ids

* Mon Sep 30 2013 Michael Mraka <michael.mraka@redhat.com> 2.1.2-1
- removed trailing whitespaces

* Thu Jul 25 2013 Stephen Herr <sherr@redhat.com> 2.1.1-1
- 988428 - Mark spacewalk-koan as correctly requiring the xz package
- Bumping package versions for 2.1.

