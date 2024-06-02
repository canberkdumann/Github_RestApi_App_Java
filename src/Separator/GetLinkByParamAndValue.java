package Separator;

public class GetLinkByParamAndValue {

    public static String getLinkFromLinkHeaderByParamAndValue(String header, String param, String value) {
        if (header != null && param != null && value != null && !"".equals(header.trim()) && !"".equals(param.trim())
                && !"".equals(value)) {

            String[] links = header.split(",");

            LINKS_LOOP:
            for (String link : links) {

                String[] segments = link.split(";");

                if (segments != null) {

                    String segmentLink = "";

                    SEGMENT_LOOP:
                    for (String segment : segments) {
                        segment = segment.trim();
                        if (segment.startsWith("<") && segment.endsWith(">")) {

                            segmentLink = segment.substring(1, segment.length() - 1);
                            continue SEGMENT_LOOP;

                        } else {
                            if (segment.split("=").length > 1) {

                                String currentSegmentParam = segment.split("=")[0].trim();
                                String currentSegmentValue = segment.split("=")[1].trim();

                                if (param.equals(currentSegmentParam) && value.equals(currentSegmentValue)) {
                                    return segmentLink;
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
}
