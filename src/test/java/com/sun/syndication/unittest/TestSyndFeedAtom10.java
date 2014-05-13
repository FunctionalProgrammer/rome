/*
 * Created on Jun 24, 2004
 *
 */
package com.sun.syndication.unittest;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.sun.syndication.feed.atom.Entry;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndLink;
import com.sun.syndication.io.impl.DateParser;

/**
 * @author pat
 * @author Dave Johnson (modified for Atom 1.0)
 *
 */
public class TestSyndFeedAtom10 extends TestSyndFeedAtom03 {

    public TestSyndFeedAtom10() {
        super("atom_1.0");
    }

    protected TestSyndFeedAtom10(final String type) {
        super(type);
    }

    protected TestSyndFeedAtom10(final String feedType, final String feedFileName) {
        super(feedType, feedFileName);
    }

    @Override
    public void testTitle() throws Exception {
        assertProperty(this.getCachedSyndFeed().getTitle(), "feed.title");
        assertProperty(this.getCachedSyndFeed().getTitleEx().getValue(), "feed.title");
        assertEquals("html", this.getCachedSyndFeed().getTitleEx().getType());

        final List<SyndLink> altLinks = this.getCachedSyndFeed().getLinks();
        assertEquals(3, altLinks.size());

        assertEquals("http://example.com/blog", altLinks.get(0).getHref());
        assertEquals("text/html", altLinks.get(0).getType());

        assertEquals("http://example.com/blog_plain", altLinks.get(1).getHref());
        assertEquals("text/plain", altLinks.get(1).getType());
    }

    @Override
    public void testLink() throws Exception {
        assertEquals(this.getCachedSyndFeed().getLink(), "http://example.com/blog");
    }

    @Override
    public void getAuthor() throws Exception {
        assertProperty(this.getCachedSyndFeed().getAuthor(), "feed.author.name");
    }

    @Override
    public void testCopyright() throws Exception {
        assertProperty(this.getCachedSyndFeed().getCopyright(), "feed.copyright");
    }

    public void testForeignMarkup() throws Exception {
        assertEquals(1, this.getCachedSyndFeed().getForeignMarkup().size());
    }

    @Override
    public void testPublishedDate() throws Exception {
        final Date d = DateParser.parseW3CDateTime("2000-01-01T00:00:00Z", Locale.US);
        assertEquals(this.getCachedSyndFeed().getPublishedDate(), d);
    }

    @Override
    protected void testEntry(final int i) throws Exception {
        final List<SyndEntry> items = this.getCachedSyndFeed().getEntries();
        final SyndEntry entry = items.get(i);

        assertProperty(entry.getTitle(), "feed.entry[" + i + "].title");
        assertProperty(entry.getTitleEx().getValue(), "feed.entry[" + i + "].title");
        assertEquals("text", entry.getTitleEx().getType());

        assertEquals("http://example.com/blog/entry" + (i + 1), entry.getLink());
        assertEquals(entry.getEnclosures().get(0).getUrl(), "http://example.com/blog/enclosure" + (i + 1) + ".gif");
        assertProperty(entry.getAuthor(), "feed.entry[" + i + "].author.name");
        final Date d = DateParser.parseW3CDateTime("2000-0" + (i + 1) + "-01T01:00:00Z", Locale.US);
        assertEquals(entry.getPublishedDate(), d);
        assertProperty(entry.getDescription().getValue(), "feed.entry[" + i + "].summary");
        assertProperty(entry.getContents().get(0).getValue(), "feed.entry[" + i + "].content[0]");
        assertEquals(1, entry.getForeignMarkup().size());

        if (i == 0) {
            final List<SyndLink> links = entry.getLinks();
            assertEquals(4, links.size());

            assertEquals("http://example.com/blog/entry1", links.get(0).getHref());
            assertEquals("text/html", links.get(0).getType());

            assertEquals("http://example.com/blog/entry1_plain", links.get(1).getHref());
            assertEquals("text/plain", links.get(1).getType());

            final SyndLink slink = entry.getLinks().get(3);
            assertTrue(slink.getHref().startsWith("tag:"));
        } else {
            final SyndLink slink = entry.getLinks().get(2);
            assertTrue(slink.getHref().startsWith("tag:"));

        }
    }

    @Override
    public void testEntry0() throws Exception {
        testEntry(0);
    }

    @Override
    public void testEntry1() throws Exception {
        testEntry(1);
    }

    @Override
    public void testEntryLink() throws Exception {
        assertEquals("http://example.com/blog/entry1", getEntryLink(this.getCachedSyndFeed().getEntries().get(0)));
        assertEquals("http://example.com/blog/entry2", getEntryLink(this.getCachedSyndFeed().getEntries().get(1)));
    }

    public void testPreservedWireItems() throws Exception {
        final SyndEntry syndEntry1 = this.getCachedSyndFeed(true).getEntries().get(0);
        final Object o = syndEntry1.getWireEntry();
        assertNotNull(o);
        assertTrue(o instanceof Entry);
        if (o instanceof Entry) {
            final Entry entry = (Entry) o;
            assertEquals("atom_1.0.feed.entry[0].rights", entry.getRights());
        }
    }

}